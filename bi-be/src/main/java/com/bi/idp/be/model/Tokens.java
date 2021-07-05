package com.bi.idp.be.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Tokens {
    private Long expiresIn;

    private String accessToken;

    private String refreshToken;

    public Tokens() {
    }

    public Tokens(Long expiresIn, String accessToken, String refreshToken) {
        this.expiresIn = expiresIn;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @JsonGetter("expires_in")
    public Long getExpiresIn() {
        return expiresIn;
    }

    @JsonSetter("expires_in")
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonGetter("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonSetter("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonGetter("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonSetter("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}
