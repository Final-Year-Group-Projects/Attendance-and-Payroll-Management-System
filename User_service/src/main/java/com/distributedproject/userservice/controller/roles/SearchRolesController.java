package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.SearchRoleByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class SearchRolesController {

    private static final Logger logger = LoggerFactory.getLogger(SearchRolesController.class);

    @Autowired
    private SearchRoleByNameService roleService;

    @GetMapping("user/get/roles/search")
    public List<Role> searchRoles(@RequestParam String name) {
        logger.info("Received request to search for roles with name: {}", name);
        List<Role> roles = roleService.searchRolesByName(name);
        if (roles.isEmpty()) {
            logger.warn("No roles found with name: {}", name);
        } else {
            logger.info("Found {} roles with name: {}", roles.size(), name);
        }
        return roles;
    }
}
