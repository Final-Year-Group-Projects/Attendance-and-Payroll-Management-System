package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.service.roles.DeleteRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteRoleController {

    @Autowired
    private DeleteRoleService roleService;

    @DeleteMapping("/delete/roles/{roleId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }
}
