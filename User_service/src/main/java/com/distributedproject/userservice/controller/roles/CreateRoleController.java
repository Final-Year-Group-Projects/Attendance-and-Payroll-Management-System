package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.service.roles.CreateRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CreateRoleController {

    private static final Logger logger = LoggerFactory.getLogger(CreateRoleController.class);

    @Autowired
    private CreateRoleService roleService;

    @PostMapping("/create/roles")
    public Role createRole(@Valid @RequestBody Role role) {
        logger.info("Received user creation request for user: {}", role);

        Role createdRole = roleService.createRole(role);
        logger.info("Role created successfully: {}", createdRole);

        return createdRole;
    }
}
