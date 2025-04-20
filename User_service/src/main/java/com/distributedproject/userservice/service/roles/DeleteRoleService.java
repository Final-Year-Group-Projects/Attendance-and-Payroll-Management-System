package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteRoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void deleteRole(Long roleId) {
        if (roleRepository.existsById(String.valueOf(roleId))) {
            roleRepository.deleteById(String.valueOf(roleId));
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }
}
