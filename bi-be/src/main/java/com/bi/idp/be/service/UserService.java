package com.bi.idp.be.service;

import com.bi.idp.be.builder.PageableBuilder;
import com.bi.idp.be.builder.UserSpecificationBuilder;
import com.bi.idp.be.config.user.UserContextHolder;
import com.bi.idp.be.controller.UserDTO;
import com.bi.idp.be.dto.SignUpDTO;
import com.bi.idp.be.exception.auth.PasswordsDontMatchException;
import com.bi.idp.be.exception.auth.TokenValidationException;
import com.bi.idp.be.exception.auth.UserAlreadyExistsHttpException;
import com.bi.idp.be.exception.auth.UserNotFoundHttpException;
import com.bi.idp.be.exception.user.AccessTokenNotFoundHttpException;
import com.bi.idp.be.exception.user.UserAlreadyExistsException;
import com.bi.idp.be.exception.user.UserNotFoundException;
import com.bi.idp.be.model.*;
import com.bi.idp.be.model.filter.UsersGridFilter;
import com.bi.idp.be.model.administrator.AdminAccount;
import com.bi.idp.be.repository.ImageRepository;
import com.bi.idp.be.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"checkstyle:ParameterNumber"})
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private RoleService roleService;
    private ImageRepository imageRepository;
    private SettingsService settingsService;
    private AuthenticationTokenService authenticationTokenService;
    private PageableBuilder pageableBuilder;

    @Value("${user.defaultImage}")
    private String defaultImage;

    @Autowired
    private TokenService tokenService;

    public static final Integer DEFAULT_AGE = 18;


    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper,
                       SettingsService settingsService,
                       RoleService roleService,
                       ImageRepository imageRepository,
                       AuthenticationTokenService authenticationTokenService,
                       PageableBuilder pageableBuilder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.settingsService = settingsService;
        this.authenticationTokenService = authenticationTokenService;
        this.roleService = roleService;
        this.imageRepository = imageRepository;
        this.pageableBuilder = pageableBuilder;
    }

    public AdminAccount findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    @Transactional
    public AdminAccount register(SignUpDTO signUpDTO) throws UserAlreadyExistsException {
        if (!signUpDTO.getPassword().equals(signUpDTO.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }

        String email = signUpDTO.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        AdminAccount user = signUpUser(signUpDTO);

        imageRepository.save(user.getImage());

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        AdminAccount user = changePasswordRequest.getUser();

        String encodedPassword = encodePassword(changePasswordRequest.getPassword());
        user.setPasswordHash(encodedPassword);

        userRepository.save(user);
    }

    public UserDTO getUserById(Long id) {
        AdminAccount existingUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundHttpException("User with id: " + id + " not found", HttpStatus.NOT_FOUND)
        );

        return modelMapper.map(existingUser, UserDTO.class);
    }

    @Transactional
    public UserDTO updateUserById(Long userId, UserDTO userDTO) {
        try {
            return updateUser(userId, userDTO);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsHttpException();
        }
    }

    @Transactional
    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundHttpException("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    public UserDTO getCurrentUser() {
        AdminAccount user = UserContextHolder.getUser();
        user.setSettings(settingsService.getSettingsByUserId(user.getId()));

        return modelMapper.map(user, UserDTO.class);
    }

    public Tokens updateCurrentUser(UserDTO userDTO) {
        try {
            AdminAccount user = UserContextHolder.getUser();
            Long id = user.getId();
            UserDTO updatedUser = updateUser(id, userDTO);
            user = modelMapper.map(updatedUser, AdminAccount.class);
            return tokenService.createToken(user);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsHttpException();
        }
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        try {
            AdminAccount user = modelMapper.map(userDTO, AdminAccount.class);

            String email = user.getEmail();
            if (userRepository.findByEmail(email).isPresent()) {
                throw new UserAlreadyExistsException(email);
            }

            // In current version password and role are default
            user.setPasswordHash(encodePassword("testPass"));
            user.setRoles(new HashSet<>(Collections.singletonList(roleService.getDefaultRole())));
            user.setImage(new Image());
            userDTO.setImageBase64(defaultImage);

            imageRepository.save(user.getImage());
            userRepository.save(user);

            return modelMapper.map(user, UserDTO.class);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsHttpException();
        }
    }

    private Image convertBaseStringToImage(String baseString) {
        Image userImage = new Image();
        byte[] decodedString = Base64.getDecoder().decode(baseString.getBytes(StandardCharsets.UTF_8));
        userImage.setImageBytes(decodedString);
        return userImage;
    }

    private UserDTO updateUser(Long id, UserDTO userDTO) throws UserAlreadyExistsException {
        AdminAccount existingUser = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundHttpException(
                        "User with id: " + id + " not found", HttpStatus.NOT_FOUND)
                );


        AdminAccount updatedUser = modelMapper.map(userDTO, AdminAccount.class);
        String updatedUserEmail = updatedUser.getEmail();
        if (!existingUser.getEmail().equals(updatedUserEmail)
                && userRepository.findByEmail(updatedUserEmail).isPresent()) {
            throw new UserAlreadyExistsException(updatedUserEmail);
        }

        updatedUser.setId(id);
        updatedUser.setPasswordHash(existingUser.getPasswordHash());
        // Current version doesn't update roles
        updatedUser.setRoles(existingUser.getRoles());
        updatedUser.setImage(existingUser.getImage());
        userRepository.save(updatedUser);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    private AdminAccount signUpUser(SignUpDTO signUpDTO) {
        AdminAccount user = new AdminAccount();
        user.setEmail(signUpDTO.getEmail());
        user.setLogin(signUpDTO.getFullName());
        String encodedPassword = encodePassword(signUpDTO.getPassword());
        user.setPasswordHash(encodedPassword);
        user.setAge(DEFAULT_AGE);
        user.setRoles(new HashSet<>(Collections.singletonList(roleService.getDefaultRole())));
        //Set default settings and image
        user.setSettings(new Settings("cosmic"));
        user.setImage(new Image());

        return user;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Image getImageById(Long id, String token) {
        try {
            authenticationTokenService.createToken(token);
        } catch (TokenValidationException e) {
            throw new AccessTokenNotFoundHttpException("Access token wasn't found", HttpStatus.NOT_FOUND);
        }

        Image existingImage = imageRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("image is not exist")
                );
        if (existingImage.getImageBytes() == null) {
            existingImage = convertBaseStringToImage(defaultImage);
            existingImage.setId(id);
        }
        return existingImage;
    }

    public Image updateUserImageById(Long id, String baseString) {
        imageRepository.findById(id).
                orElseThrow(() -> new RuntimeException("image is not exist"));
        Image existingImage;
        if (baseString != null) {
            existingImage = convertBaseStringToImage(baseString);
        } else {
            existingImage = new Image();
        }
        existingImage.setId(id);

        imageRepository.save(existingImage);

        return existingImage;
    }

    private List<UserDTO> mapOrdersToOrderDTO(List<AdminAccount> orders) {
        return orders.stream().map(order -> {
            UserDTO dto = modelMapper.map(order, UserDTO.class);
//            if (dto.getAddress() == null) {
//                dto.setAddress(new AddressDTO());
//            }
            return dto;
        }).collect(Collectors.toList());
    }

    private GridData<UserDTO> parsePageToGridData(Page<AdminAccount> orderPages) {
        GridData<UserDTO> gridData = new GridData<>();
        List<AdminAccount> orderList = orderPages.getContent();
        long totalCount = orderPages.getTotalElements();
        gridData.setItems(mapOrdersToOrderDTO(orderList));
        gridData.setTotalCount(totalCount);
        return gridData;
    }

    public GridData<UserDTO> getDataForGrid(UsersGridFilter filter) {
        UserSpecificationBuilder specificationBuilder = new UserSpecificationBuilder();

        Pageable paginationAndSort = pageableBuilder.build(filter);
        Optional<Specification<AdminAccount>> optionalSpec = specificationBuilder.build(filter);
        Page<AdminAccount> orderPages = optionalSpec
                .map(userSpecification -> userRepository.findAll(userSpecification, paginationAndSort))
                .orElseGet(() -> userRepository.findAll(paginationAndSort));
        return parsePageToGridData(orderPages);
    }
}
