package com.bi.idp.be.exception.auth;

public class TokenValidationException extends AuthenticationException {

    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException() {
    }
}
