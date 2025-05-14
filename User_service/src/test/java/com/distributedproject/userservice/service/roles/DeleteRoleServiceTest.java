package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.role.RoleNotFoundException;
import com.distributedproject.userservice.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteRoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private DeleteRoleService deleteRoleService;

    @Test
    void deleteRole_WhenRoleExists_ShouldDeleteRole() {
        Long roleId = 1L;
        when(roleRepository.existsById(roleId)).thenReturn(true);

        deleteRoleService.deleteRole(roleId);

        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void deleteRole_WhenRoleDoesNotExist_ShouldThrowRoleNotFoundException() {
        Long roleId = 2L;
        when(roleRepository.existsById(roleId)).thenReturn(false);

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () -> {
            deleteRoleService.deleteRole(roleId);
        });

        assertEquals("Role with ID 2 not found", exception.getMessage());
        verify(roleRepository, never()).deleteById(roleId);
    }
}
