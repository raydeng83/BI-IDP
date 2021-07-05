package com.bi.idp.be.controller;

import com.bi.idp.be.model.GridData;
import com.bi.idp.be.model.Image;
import com.bi.idp.be.model.ResponseMessage;
import com.bi.idp.be.model.Tokens;
import com.bi.idp.be.model.filter.UsersGridFilter;
import com.bi.idp.be.service.BundleUserDetailsService;
import com.bi.idp.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for managing users
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<GridData<UserDTO>> getDataForGrid(UsersGridFilter usersGridFilter) {
        usersGridFilter = usersGridFilter == null ? new UsersGridFilter() : usersGridFilter;
        return ok(userService.getDataForGrid(usersGridFilter));
    }

    /**
     * Get user. Allowed only for Admin
     * @param id user id
     * @return user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ok(userService.getUserById(id));
    }

    /**
     * Update user. Allowed only for Admin
     * @param id user id
     * @param userDTO updated user data
     * @return updated user data
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserById(id, userDTO);
        return ok(updatedUser);
    }

    /**
     * Delete user
     * @param id user id
     * @return boolean result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(Authentication auth, @PathVariable Long id) {
        Long currentUserId = ((BundleUserDetailsService.BundleUserDetails) auth.getPrincipal()).getUser().getId();
        if (currentUserId.equals(id)) {
            return new ResponseEntity<>(
                    "It is impossible to delete the current user",
                    HttpStatus.BAD_REQUEST);
        }
        userService.deleteUser(id);
        return ok(new ResponseMessage("Ok"));
    }

    /**
     * Get current user
     * @return current user data
     */
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ok(userService.getCurrentUser());
    }

    /**
     * Update current user
     * @param userDTO updated user data
     * @return updated user data
     */
    @PutMapping("/current")
    public ResponseEntity<Tokens> updateCurrentUser(@Valid @RequestBody UserDTO userDTO) {
        return ok(userService.updateCurrentUser(userDTO));
    }

    /**
     * Create user. Allowed only for Admin
     * @param userDTO new user data
     * @return created user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ok(userService.createUser(userDTO));
    }

    /**
     * Update current user image
     *
     * @param baseString updated user image
     * @return updated image
     */
    @PutMapping("/{id}/photo")
    public ResponseEntity<Image> updateUserImage(@PathVariable Long id, @Valid @RequestBody String baseString) {
        Image image = userService.updateUserImageById(id, baseString);
        return ok(image);
    }

    /**
     * Get user image by id
     *
     * @param id user image id
     * @return user image
     */
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id, @RequestParam String token) {
        Image image = userService.getImageById(id, token);
        byte[] imageBytes = image.getImageBytes();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
