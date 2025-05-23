package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.exception.role.RoleNameAlreadyExistsException;
import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateRoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role updateRole(Long roleId, Role roleDetails) {
        Optional<Role> existingRole = roleRepository.findById(roleId);

        if (existingRole.isPresent()) {
            // Check if new name already exists in another role
            Optional<Role> roleByName = roleRepository.findByRoleNameIgnoreCase(roleDetails.getRoleName());
            if (roleByName.isPresent() && !roleByName.get().getRoleId().equals(roleId)) {
                throw new RoleNameAlreadyExistsException(roleDetails.getRoleName());
            }

            Role roleToUpdate = existingRole.get();
            roleToUpdate.setRoleName(roleDetails.getRoleName());

            return roleRepository.save(roleToUpdate);
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }
}
