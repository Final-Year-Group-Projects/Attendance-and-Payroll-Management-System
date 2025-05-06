package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.department.SearchDepartmentByNameService;
import com.distributedproject.userservice.service.roles.SearchRoleByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchRolesController {

    @Autowired
    private SearchRoleByNameService roleService;

    @GetMapping("/get/roles/search")
    public List<Role> searchUsers(@RequestParam String name) {
        return roleService.searchRolesByName(name);
    }
}
