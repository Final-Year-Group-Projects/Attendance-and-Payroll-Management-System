package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.UpdateRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UpdateRolesController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRolesController.class);

    @Autowired
    private UpdateRoleService updateRoleService;  // Autowire the correct service class

    @PutMapping("/user/update/roles/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @Valid @RequestBody Role roleDetails) {
        logger.info("Received request to update role with ID: {}", roleId);

        Role updatedRole = updateRoleService.updateRole(roleId, roleDetails);

        // If role is not found, throw exception
        if (updatedRole == null) {
            logger.error("Role with ID {} not found. Throwing exception.", roleId);
            throw new DepartmentNotFoundException(roleId); // Trigger custom exception
        }

        // If role is found and updated
        logger.info("Role with ID {} updated successfully.", roleId);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }
}
