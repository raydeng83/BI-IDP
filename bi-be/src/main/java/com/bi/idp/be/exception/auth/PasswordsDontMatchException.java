package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PasswordsDontMatchException extends HttpException {

    private static final long serialVersionUID = -7852550573176915476L;

    public PasswordsDontMatchException() {
        super("Passwords don't match", HttpStatus.BAD_REQUEST);
    }
}
