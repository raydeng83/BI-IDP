package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidTokenHttpException extends HttpException {
    private static final long serialVersionUID = 773684525186809237L;

    public InvalidTokenHttpException() {
        super(HttpStatus.FORBIDDEN);
    }
}
