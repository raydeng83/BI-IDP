package com.bi.idp.be.model;

import com.bi.idp.be.model.administrator.AdminAccount;

public class ChangePasswordRequest {
    private AdminAccount user;
    private String password;

    public AdminAccount getUser() {
        return user;
    }

    public void setUser(AdminAccount user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
