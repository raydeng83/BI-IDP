package com.bi.idp.be.config.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "jwt")
public class Properties {

    private String accessTokenSecretKey;

    private String refreshTokenSecretKey;

    private long accessTokenValidityInMilliseconds;

    private long refreshTokenValidityInMilliseconds;

    public void setAccessTokenSecretKey(String accessTokenSecretKey) {
        this.accessTokenSecretKey = Base64
                .getEncoder()
                .encodeToString(accessTokenSecretKey
                        .getBytes(UTF_8));
    }

    public void setRefreshTokenSecretKey(String refreshTokenSecretKey) {
        this.refreshTokenSecretKey = refreshTokenSecretKey;
    }

    public void setAccessTokenValidityInMilliseconds(long accessTokenValidityInMilliseconds) {
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
    }

    public void setRefreshTokenValidityInMilliseconds(long refreshTokenValidityInMilliseconds) {
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    public String getAccessTokenSecretKey() {
        return accessTokenSecretKey;
    }

    public String getRefreshTokenSecretKey() {
        return refreshTokenSecretKey;
    }

    public long getAccessTokenValidityInMilliseconds() {
        return accessTokenValidityInMilliseconds;
    }

    public long getRefreshTokenValidityInMilliseconds() {
        return refreshTokenValidityInMilliseconds;
    }
}
