package com.bi.idp.be.controller;

import com.bi.idp.be.dto.*;
import com.bi.idp.be.exception.auth.PasswordsDontMatchException;
import com.bi.idp.be.model.ResponseMessage;
import com.bi.idp.be.model.Tokens;
import com.bi.idp.be.service.AuthService;
import com.bi.idp.be.service.RequestPasswordService;
import com.bi.idp.be.service.ResetPasswordService;
import com.bi.idp.be.service.RestorePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller which provides functionality for authentication
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RequestPasswordService requestPasswordService;
    private final RestorePasswordService restorePasswordService;
    private final ResetPasswordService resetPasswordService;

    @Autowired
    public AuthController(AuthService authService,
                          RequestPasswordService requestPasswordService,
                          RestorePasswordService restorePasswordService,
                          ResetPasswordService resetPasswordService) {
        this.authService = authService;
        this.requestPasswordService = requestPasswordService;
        this.restorePasswordService = restorePasswordService;
        this.resetPasswordService = resetPasswordService;
    }

    /**
     * Login user
     * @param loginDTO user credentials
     * @return generated token
     */
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDTO loginDTO) {
        Tokens tokens = authService.login(loginDTO);
        return toResponse(tokens);
    }

    /**
     * Restore password
     * @param restorePasswordDTO new password with token
     * @return result message
     */
    @PostMapping("/restore-pass")
    public ResponseEntity<ResponseMessage> restorePassword(@Valid @RequestBody RestorePasswordDTO restorePasswordDTO) {
        if (!restorePasswordDTO.getNewPassword().equals(restorePasswordDTO.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }

        restorePasswordService.restorePassword(restorePasswordDTO);
        return ok(new ResponseMessage("Password was restored"));
    }

    /**
     * Sign up
     * @param signUpDTO sign up user data
     * @return token
     */
    @PostMapping("/sign-up")
    public ResponseEntity register(@Valid @RequestBody SignUpDTO signUpDTO) {
        if (!signUpDTO.getPassword().equals(signUpDTO.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }

        Tokens tokens = authService.register(signUpDTO);
        return toResponse(tokens);
    }

    /**
     * Request password. Generate link for restoring password which should be sent via email
     * @param requestPasswordDTO object with user email
     * @return result message
     */
    @PostMapping("/request-pass")
    public ResponseEntity<ResponseMessage> requestPassword(@Valid @RequestBody RequestPasswordDTO requestPasswordDTO) {
        requestPasswordService.requestPassword(requestPasswordDTO);
        return ok(new ResponseMessage("Ok"));
    }

    /**
     * Sign out. Perform any required actions to log out user, like invalidate user session.
     * Implement your required logic
     * @return result message
     */
    @PostMapping("/sign-out")
    public ResponseEntity<ResponseMessage> logout() {
        return ok(new ResponseMessage("Ok"));
    }

    /**
     * Reset password for signed in user
     * @param resetPasswordDTO new and confirmed passwords
     * @return result message
     */
    @PostMapping("/reset-pass")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        if (!resetPasswordDTO.getConfirmPassword().equals(resetPasswordDTO.getPassword())) {
            throw new PasswordsDontMatchException();
        }

        resetPasswordService.resetPassword(resetPasswordDTO);
        return ok(new ResponseMessage("Password was reset"));
    }

    /**
     * Refresh token
     * @param refreshTokenDTO refresh token
     * @return new token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        Tokens tokens = authService.refreshToken(refreshTokenDTO);
        return toResponse(tokens);
    }

    private ResponseEntity<RefreshTokenDTO> toResponse(Tokens tokens) {
        return ok(new RefreshTokenDTO(tokens));
    }
}
