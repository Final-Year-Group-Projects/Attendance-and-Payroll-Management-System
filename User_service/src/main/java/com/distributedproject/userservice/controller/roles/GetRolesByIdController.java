package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.model.Role;

import com.distributedproject.userservice.service.roles.GetRolesByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetRolesByIdController {

    @Autowired
    private GetRolesByIdService roleService;

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Role> getRolesById(@PathVariable Long roleId) {
        Role role = roleService.getRolesById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        return ResponseEntity.ok(role);
    }
}
