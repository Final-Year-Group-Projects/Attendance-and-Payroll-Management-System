package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.GetAllRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class GetRolesController {

    private static final Logger logger = LoggerFactory.getLogger(GetRolesController.class);

    @Autowired
    private GetAllRolesService rolesService;

    @GetMapping("/user/getAll/roles")
    public List<Role> getRoles() {
        logger.info("Received request to fetch all roles.");
        List<Role> roles = rolesService.getAllRoles();
        logger.info("Successfully fetched all roles, total roles: {}", roles.size());
        return roles;
    }
}
