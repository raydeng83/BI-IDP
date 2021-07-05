package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class CantSendEmailHttpException extends HttpException {
    public CantSendEmailHttpException() {
        super("Can't reset password, please, try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
