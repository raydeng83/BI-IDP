package com.bi.idp.be.service;

import com.bi.idp.be.builder.PageableBuilder;
import com.bi.idp.be.config.user.UserContextHolder;
import com.bi.idp.be.controller.UserDTO;
import com.bi.idp.be.dto.SignUpDTO;
import com.bi.idp.be.exception.auth.PasswordsDontMatchException;
import com.bi.idp.be.exception.auth.UserAlreadyExistsHttpException;
import com.bi.idp.be.exception.auth.UserNotFoundHttpException;
import com.bi.idp.be.exception.user.UserAlreadyExistsException;
import com.bi.idp.be.exception.user.UserNotFoundException;
import com.bi.idp.be.model.ChangePasswordRequest;
import com.bi.idp.be.model.Tokens;
import com.bi.idp.be.model.user.User;
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
                       RoleService roleService,
                       AuthenticationTokenService authenticationTokenService,
                       PageableBuilder pageableBuilder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationTokenService = authenticationTokenService;
        this.roleService = roleService;
        this.pageableBuilder = pageableBuilder;
    }

    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    @Transactional
    public User register(SignUpDTO signUpDTO) throws UserAlreadyExistsException {
        if (!signUpDTO.getPassword().equals(signUpDTO.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }

        String email = signUpDTO.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        User user = signUpUser(signUpDTO);

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = changePasswordRequest.getUser();

        String encodedPassword = encodePassword(changePasswordRequest.getPassword());
        user.setPasswordHash(encodedPassword);

        userRepository.save(user);
    }

    public UserDTO getUserById(Long id) {
        User existingUser = userRepository.findById(id).orElseThrow(
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
        User user = UserContextHolder.getUser();

        return modelMapper.map(user, UserDTO.class);
    }

    public Tokens updateCurrentUser(UserDTO userDTO) {
        try {
            User user = UserContextHolder.getUser();
            Long id = user.getId();
            UserDTO updatedUser = updateUser(id, userDTO);
            user = modelMapper.map(updatedUser, User.class);
            return tokenService.createToken(user);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsHttpException();
        }
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        try {
            User user = modelMapper.map(userDTO, User.class);

            String email = user.getEmail();
            if (userRepository.findByEmail(email).isPresent()) {
                throw new UserAlreadyExistsException(email);
            }

            // In current version password and role are default
            user.setPasswordHash(encodePassword("testPass"));
            user.setRoles(new HashSet<>(Collections.singletonList(roleService.getDefaultRole())));
            userDTO.setImageBase64(defaultImage);

            userRepository.save(user);

            return modelMapper.map(user, UserDTO.class);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsHttpException();
        }
    }

    private UserDTO updateUser(Long id, UserDTO userDTO) throws UserAlreadyExistsException {
        User existingUser = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundHttpException("User with id: " + id + " not found", HttpStatus.NOT_FOUND));

        User updatedUser = modelMapper.map(userDTO, User.class);
        String updatedUserEmail = updatedUser.getEmail();
        if (!existingUser.getEmail().equals(updatedUserEmail)
                && userRepository.findByEmail(updatedUserEmail).isPresent()) {
            throw new UserAlreadyExistsException(updatedUserEmail);
        }

        updatedUser.setId(id);
        updatedUser.setPasswordHash(existingUser.getPasswordHash());
        // Current version doesn't update roles
        updatedUser.setRoles(existingUser.getRoles());
        userRepository.save(updatedUser);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    private User signUpUser(SignUpDTO signUpDTO) {
        User user = new User();
        user.setEmail(signUpDTO.getEmail());
        user.setLogin(signUpDTO.getFullName());
        String encodedPassword = encodePassword(signUpDTO.getPassword());
        user.setPasswordHash(encodedPassword);
        user.setAge(DEFAULT_AGE);
        user.setRoles(new HashSet<>(Collections.singletonList(roleService.getDefaultRole())));

        return user;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
