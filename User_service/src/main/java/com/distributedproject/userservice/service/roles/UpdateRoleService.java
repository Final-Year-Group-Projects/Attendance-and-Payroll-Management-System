package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateRoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role updateRole(Long roleId, Role roleDetails) {
        // Use Optional to find the user by ID
        Optional<Role> existingRole = roleRepository.findById(String.valueOf(roleId));

        // If user is found, update the user details
        if (existingRole.isPresent()) {
            Role roleToUpdate = existingRole.get();
            roleToUpdate.setRoleName(roleDetails.getRoleName());
            roleToUpdate.setRoleDescription(roleDetails.getRoleDescription());

            // Save the updated user to the repository
            return roleRepository.save(roleToUpdate);
        } else {
            // If user is not found, throw custom exception
            throw new RoleNotFoundException(roleId);  // Use custom exception here
        }
    }
}
