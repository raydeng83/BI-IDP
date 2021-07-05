package com.bi.idp.be.exception.auth;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundOrExpiredHttpException extends HttpException {
    public TokenNotFoundOrExpiredHttpException() {
        super("Reset password request wasn't performed or already expired", HttpStatus.FORBIDDEN);
    }
}
