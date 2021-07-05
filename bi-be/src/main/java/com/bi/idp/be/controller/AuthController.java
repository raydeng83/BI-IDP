package com.bi.idp.be.controller;

import com.bi.idp.be.dto.LoginDTO;
import com.bi.idp.be.dto.RefreshTokenDTO;
import com.bi.idp.be.dto.SignUpDTO;
import com.bi.idp.be.exception.auth.InvalidTokenHttpException;
import com.bi.idp.be.exception.auth.PasswordsDontMatchException;
import com.bi.idp.be.exception.auth.UserAlreadyExistsHttpException;
import com.bi.idp.be.model.ResponseMessage;
import com.bi.idp.be.model.Tokens;
import com.bi.idp.be.service.AuthService;
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

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
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
     * Sign up
     * @param signUpDTO sign up user data
     * @return token
     */
    @PostMapping("/sign-up")
    public ResponseEntity register(@Valid @RequestBody SignUpDTO signUpDTO)  {
        if (!signUpDTO.getPassword().equals(signUpDTO.getConfirmPassword())) {
            throw new PasswordsDontMatchException();
        }

        Tokens tokens = authService.register(signUpDTO);
        return toResponse(tokens);
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
     * Refresh token
     * @param refreshTokenDTO refresh token
     * @return new token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) throws InvalidTokenHttpException {
        Tokens tokens = authService.refreshToken(refreshTokenDTO);
        return toResponse(tokens);
    }

    private ResponseEntity<RefreshTokenDTO> toResponse(Tokens tokens) {
        return ok(new RefreshTokenDTO(tokens));
    }
}
