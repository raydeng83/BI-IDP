package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class IncorrectEmailHttpException extends HttpException {
    public IncorrectEmailHttpException() {
        // TODO check http status
        super("Email is invalid or doesn't registered", HttpStatus.FORBIDDEN);
    }
}
