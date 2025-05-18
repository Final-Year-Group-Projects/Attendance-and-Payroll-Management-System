package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetRolesByIdService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> getRolesById(Long roleId) {
        return roleRepository.findById(roleId);
    }
}
