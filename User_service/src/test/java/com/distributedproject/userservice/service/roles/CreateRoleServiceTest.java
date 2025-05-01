package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateRoleServiceTest {

    private RoleRepository roleRepository;
    private CreateRoleService createRoleService;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        createRoleService = new CreateRoleService(roleRepository);  // Constructor injection
    }

    @Test
    void createRole_successfulSave() {
        // Arrange
        Role role = new Role();
        role.setRoleName("Admin");

        when(roleRepository.existsByRoleNameIgnoreCase("Admin")).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role result = createRoleService.createRole(role);

        // Assert
        assertNotNull(result);
        assertEquals("Admin", result.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRole_nameAlreadyExists_throwsException() {
        // Arrange
        Role role = new Role();
        role.setRoleName("Admin");

        when(roleRepository.existsByRoleNameIgnoreCase("Admin")).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> createRoleService.createRole(role)
        );

        assertEquals("400 BAD_REQUEST \"Role name already taken.\"", exception.getMessage());
        verify(roleRepository, never()).save(any(Role.class));
    }
}
