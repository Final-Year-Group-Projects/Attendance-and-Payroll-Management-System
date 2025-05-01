package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CreateRoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public CreateRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        if (roleRepository.existsByRoleNameIgnoreCase(role.getRoleName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role name already taken.");
        }

        return roleRepository.save(role);
    }
}
