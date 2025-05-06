package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.GetAllRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetRolesController {

    @Autowired
    private GetAllRolesService rolesService;

    @GetMapping("/get/roles")
    public List<Role> getRoles() {
        return rolesService.getAllRoles();
    }
}
