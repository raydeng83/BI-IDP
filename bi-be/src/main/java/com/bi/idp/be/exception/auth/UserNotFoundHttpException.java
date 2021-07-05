package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class UserNotFoundHttpException extends HttpException {
    private static final long serialVersionUID = 4770986620665158856L;

    public UserNotFoundHttpException(String message, HttpStatus status) {
        super(message, status);
    }
}
