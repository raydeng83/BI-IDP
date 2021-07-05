package com.bi.idp.be.service;

import com.bi.idp.be.model.role.Role;
import com.bi.idp.be.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getDefaultRole() {
        return roleRepository.findDefault();
    }

}
