package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.role.RoleNameNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchRoleByNameService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> searchRolesByName(String name) {
        List<Role> roles = roleRepository.findByRoleNameContainingIgnoreCase(name);
        if (roles.isEmpty()) {
            throw new RoleNameNotFoundException(name);
        }
        return roles;
    }
}
