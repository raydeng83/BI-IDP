package com.bi.idp.be.model;

import com.bi.idp.be.model.user.User;

public class ChangePasswordRequest {
    private User user;
    private String password;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
