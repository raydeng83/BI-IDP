package com.bi.idp.be.dto;

import com.bi.idp.be.model.Tokens;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RefreshTokenDTO {

    @NotNull
    @JsonProperty("token")
    private Tokens tokens;

    public RefreshTokenDTO() {
    }

    public RefreshTokenDTO(@NotEmpty @NotNull Tokens tokens) {
        this.tokens = tokens;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }
}
