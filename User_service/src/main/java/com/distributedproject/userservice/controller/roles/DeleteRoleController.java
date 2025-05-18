package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.service.roles.DeleteRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DeleteRoleController {

    private static final Logger logger = LoggerFactory.getLogger(DeleteRoleController.class);

    @Autowired
    private DeleteRoleService roleService;

    @DeleteMapping("/delete/roles/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
        logger.info("Received request to delete role with ID: {}", roleId);
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Role deleted successfully with ID: " + roleId);
    }
}
