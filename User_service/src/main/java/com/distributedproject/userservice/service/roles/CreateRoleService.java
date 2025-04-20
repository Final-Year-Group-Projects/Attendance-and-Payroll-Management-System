package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRoleService {

    @Autowired
    private RoleRepository RoleRepository;

    public Role createRole(Role role) {
        return RoleRepository.save(role);
    }
}
