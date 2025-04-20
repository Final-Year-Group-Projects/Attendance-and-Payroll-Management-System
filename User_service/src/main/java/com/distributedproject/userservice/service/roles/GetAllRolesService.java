package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllRolesService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
