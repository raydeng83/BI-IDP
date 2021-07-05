package com.bi.idp.be.service;

import com.bi.idp.be.dto.LoginDTO;
import com.bi.idp.be.dto.RefreshTokenDTO;
import com.bi.idp.be.dto.SignUpDTO;
import com.bi.idp.be.exception.auth.InvalidTokenHttpException;
import com.bi.idp.be.exception.auth.UserAlreadyExistsHttpException;
import com.bi.idp.be.exception.auth.UserNotFoundHttpException;
import com.bi.idp.be.exception.user.UserAlreadyExistsException;
import com.bi.idp.be.exception.user.UserNotFoundException;
import com.bi.idp.be.model.Tokens;
import com.bi.idp.be.model.administrator.AdminAccount;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private SettingsService settingsService;

    @Autowired
    public AuthService(UserService userService,
                       SettingsService settingsService,
                       AuthenticationManager authenticationManager,
                       TokenService tokenService) {
        this.settingsService = settingsService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Tokens register(SignUpDTO signUpDTO) throws UserAlreadyExistsHttpException {
        try {
            AdminAccount user = userService.register(signUpDTO);
            return createToken(user);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsHttpException();
        }
    }

    public Tokens login(LoginDTO loginDTO) throws UserNotFoundHttpException {
        try {
            Authentication authentication = createAuthentication(loginDTO);
            BundleUserDetailsService.BundleUserDetails userDetails =
                    (BundleUserDetailsService.BundleUserDetails) authenticationManager
                            .authenticate(authentication).getPrincipal();
            AdminAccount user = userDetails.getUser();
            user.setSettings(settingsService.getSettingsByUserId(user.getId()));
            return createToken(user);
        } catch (AuthenticationException exception) {
            throw new UserNotFoundHttpException("Incorrect email or password", HttpStatus.FORBIDDEN);
        }
    }

    public Tokens refreshToken(RefreshTokenDTO refreshTokenDTO) throws InvalidTokenHttpException {
        try {
            String email = tokenService.getEmailFromRefreshToken(refreshTokenDTO.getTokens().getRefreshToken());
            AdminAccount user = userService.findByEmail(email);
            return createToken(user);
        } catch (JwtException | UserNotFoundException e) {
            throw new InvalidTokenHttpException();
        }
    }

    private Authentication createAuthentication(LoginDTO loginDTO) {
        return new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
    }

    private Tokens createToken(AdminAccount user) {
        return tokenService.createToken(user);
    }

}
