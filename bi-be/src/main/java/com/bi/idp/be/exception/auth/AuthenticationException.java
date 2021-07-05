package com.bi.idp.be.exception.auth;

public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException() {
    }
}
