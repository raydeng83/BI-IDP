package com.bi.idp.be.service;

import com.bi.idp.be.config.auth.AuthenticationToken;
import com.bi.idp.be.config.auth.Properties;
import com.bi.idp.be.exception.auth.TokenValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationTokenService {

    private Properties properties;

    @Autowired
    private AuthenticationTokenService(Properties properties) {
        this.properties = properties;
    }

    public AuthenticationToken createToken(String token) throws TokenValidationException {
        return new AuthenticationToken(token, properties.getAccessTokenSecretKey());
    }
}
