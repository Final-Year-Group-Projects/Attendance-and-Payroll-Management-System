package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.role.RoleNameAlreadyExistsException;
import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateRoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UpdateRoleService updateRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateRole_Successful() {
        Long roleId = 1L;
        Role existingRole = new Role();
        existingRole.setRoleId(roleId);
        existingRole.setRoleName("USER");

        Role updatedDetails = new Role();
        updatedDetails.setRoleName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(roleRepository.findByRoleNameIgnoreCase("ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Role result = updateRoleService.updateRole(roleId, updatedDetails);

        assertEquals("ADMIN", result.getRoleName());
        verify(roleRepository).save(existingRole);
    }

    @Test
    void testUpdateRole_RoleNotFound() {
        Long roleId = 99L;
        Role roleDetails = new Role();
        roleDetails.setRoleName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> updateRoleService.updateRole(roleId, roleDetails));

        assertEquals("Role with ID 99 not found", exception.getMessage());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_RoleNameAlreadyExists() {
        Long roleId = 1L;

        Role existingRole = new Role();
        existingRole.setRoleId(roleId);
        existingRole.setRoleName("USER");

        Role roleDetails = new Role();
        roleDetails.setRoleName("ADMIN");

        Role otherRole = new Role();
        otherRole.setRoleId(2L);
        otherRole.setRoleName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(roleRepository.findByRoleNameIgnoreCase("ADMIN")).thenReturn(Optional.of(otherRole));

        RoleNameAlreadyExistsException exception = assertThrows(RoleNameAlreadyExistsException.class,
                () -> updateRoleService.updateRole(roleId, roleDetails));

        assertEquals("Role name is already taken.", exception.getMessage());
        verify(roleRepository, never()).save(any());
    }
}
