package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.CreateRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateRoleController {

    @Autowired
    private CreateRoleService roleService;

    @PostMapping("/create/roles")
    public Role createRole(@Valid @RequestBody Role role) {
        System.out.println("Received role: " + role);
        return roleService.createRole(role);

    }
}
