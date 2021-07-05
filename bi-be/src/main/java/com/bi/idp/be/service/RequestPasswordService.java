package com.bi.idp.be.service;

import com.bi.idp.be.dto.RequestPasswordDTO;
import com.bi.idp.be.dto.RestorePasswordTokenDto;
import com.bi.idp.be.exception.auth.CantSendEmailHttpException;
import com.bi.idp.be.exception.auth.IncorrectEmailHttpException;
import com.bi.idp.be.exception.user.UserNotFoundException;
import com.bi.idp.be.model.RestorePassword;
import com.bi.idp.be.model.user.User;
import com.bi.idp.be.repository.RestorePasswordTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class RequestPasswordService {

    private Logger logger = LoggerFactory.getLogger(RequestPasswordService.class);

    private UserService userService;
    private RestorePasswordTokenRepository restorePasswordTokenRepository;

    @Autowired
    public RequestPasswordService(UserService userService,
                                  RestorePasswordTokenRepository restorePasswordTokenRepository) {
        this.userService = userService;
        this.restorePasswordTokenRepository = restorePasswordTokenRepository;
    }

    @Value("${client.url}")
    private String clientUrl;

    @Value("${client.resetPasswordToken.expiration}")
    private Duration resetPasswordTokenExpiration;

    public void requestPassword(RequestPasswordDTO requestPasswordDTO) {
        User user;

        try {
            user = userService.findByEmail(requestPasswordDTO.getEmail());
        } catch (UserNotFoundException exception) {
            throw new IncorrectEmailHttpException();
        }

        // generate reset password token
        RestorePassword token = new RestorePassword();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiresIn(this.calculateExpirationDate(resetPasswordTokenExpiration));
        restorePasswordTokenRepository.save(token);

        // send reset password token via email
        try {
            String resetPasswordUrl = createResetUrl(token);
            // Reset Password Token should be sent via Email. You can use reset url in your template
            logger.info("Reset url was created: {}", resetPasswordUrl);
        } catch (JsonProcessingException exception) {
            throw new CantSendEmailHttpException();
        }
    }

    private LocalDateTime calculateExpirationDate(Duration tokenExpirationDuration) {
        return LocalDateTime.now().plusMinutes(tokenExpirationDuration.toMinutes());
    }

    private String createResetUrl(RestorePassword restorePassword) throws JsonProcessingException {
        // create reset password token dto
        RestorePasswordTokenDto restorePasswordTokenDto = new RestorePasswordTokenDto();
        restorePasswordTokenDto.setExpiryDate(restorePassword.getExpiresIn());
        restorePasswordTokenDto.setToken(restorePassword.getToken());

        // map token dto to json
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(restorePasswordTokenDto);

        // encode with base64
        String encodedToken = Base64.getEncoder().encodeToString(jsonToken.getBytes(UTF_8));

        return clientUrl + "/auth/restore-pass?token=" + encodedToken;
    }
}
