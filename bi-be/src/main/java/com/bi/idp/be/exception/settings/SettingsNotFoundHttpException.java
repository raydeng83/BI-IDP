
package com.bi.idp.be.exception.settings;

import com.bi.idp.be.exception.HttpException;
import org.springframework.http.HttpStatus;

public class SettingsNotFoundHttpException extends HttpException {

    private static final long serialVersionUID = -5258959358527382145L;

    public SettingsNotFoundHttpException(String message, HttpStatus status) {
        super(message, status);
    }
}
