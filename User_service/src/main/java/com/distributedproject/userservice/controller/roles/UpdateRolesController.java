package com.distributedproject.userservice.controller.roles;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.service.roles.UpdateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateRolesController {

    @Autowired
    private UpdateRoleService updateRoleService;  // Autowire the correct service class

    @PutMapping("/roles/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role roleDetails) {
        // Now we throw UserNotFoundException if the user doesn't exist
        Role updatedRole = updateRoleService.updateRole(roleId, roleDetails);

        // If user is null or not found, throw exception (UserNotFoundException)
        if (updatedRole == null) {
            throw new DepartmentNotFoundException(roleId); // Trigger custom exception
        }

        // If user is found and updated
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }
}

