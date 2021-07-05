package com.bi.idp.be.exception.user;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class UserInvalidHttpException extends HttpException {

    private static final long serialVersionUID = 2401650728998512026L;

    public UserInvalidHttpException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
