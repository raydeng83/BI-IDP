package com.bi.idp.be.exception.user;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class AccessTokenNotFoundHttpException extends HttpException {

    public AccessTokenNotFoundHttpException(String message, HttpStatus status) {
        super(message, status);
    }
}
