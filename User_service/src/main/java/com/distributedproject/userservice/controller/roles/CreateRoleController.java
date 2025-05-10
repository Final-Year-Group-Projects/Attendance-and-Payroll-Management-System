package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Role;
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
    public ResponseEntity<String> createRole(@Valid @RequestBody Role role) {
        logger.info("Received role creation request: {}", role);
        Role savedRole = roleService.createRole(role);
        return ResponseEntity.status(201).body("Role created successfully: " + savedRole.getRoleName());
    }
}
