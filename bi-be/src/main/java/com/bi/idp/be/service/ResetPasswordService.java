package com.bi.idp.be.service;

import com.bi.idp.be.config.user.UserContextHolder;
import com.bi.idp.be.dto.ResetPasswordDTO;
import com.bi.idp.be.model.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    private UserService userService;

    @Autowired
    public ResetPasswordService(UserService userService) {
        this.userService = userService;
    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUser(UserContextHolder.getUser());
        changePasswordRequest.setPassword(resetPasswordDTO.getPassword());
        userService.changePassword(changePasswordRequest);
    }

}
