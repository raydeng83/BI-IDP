package com.bi.idp.be.exception.user;

import com.bi.idp.be.exception.ApiException;

public class UserNotFoundException extends ApiException {
    private static final long serialVersionUID = -5258959358527382145L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
