package com.bi.idp.be.config.user;

import com.bi.idp.be.model.administrator.AdminAccount;
import com.bi.idp.be.service.BundleUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContextHolder {

    private UserContextHolder() {
    }

    public static AdminAccount getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BundleUserDetailsService.BundleUserDetails userDetails = (BundleUserDetailsService.BundleUserDetails) principal;
        return userDetails.getUser();
    }
}
