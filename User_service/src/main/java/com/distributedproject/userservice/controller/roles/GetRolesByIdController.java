package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.GetRolesByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GetRolesByIdController {

    private static final Logger logger = LoggerFactory.getLogger(GetRolesByIdController.class);

    @Autowired
    private GetRolesByIdService roleService;

    @GetMapping("/get/roles/{roleId}")
    public ResponseEntity<Role> getRolesById(@PathVariable Long roleId) {
        logger.info("Received request to get role with ID: {}", roleId);
        Role role = roleService.getRolesById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        logger.info("Successfully fetched role with ID: {}", roleId);
        return ResponseEntity.ok(role);
    }
}
