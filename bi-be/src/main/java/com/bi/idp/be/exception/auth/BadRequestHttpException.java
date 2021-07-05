package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class BadRequestHttpException extends HttpException {
    private static final long serialVersionUID = -5202433948475658078L;

    public BadRequestHttpException() {
        super("Bad request", HttpStatus.BAD_REQUEST);
    }
}
