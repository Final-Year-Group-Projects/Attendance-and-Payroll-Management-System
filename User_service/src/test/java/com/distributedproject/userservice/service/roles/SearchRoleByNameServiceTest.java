package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.exception.role.RoleNameNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SearchRoleByNameServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private SearchRoleByNameService searchRoleByNameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchRolesByName_Found() {
        // Arrange
        String roleName = "admin";
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("Admin");

        List<Role> mockRoles = List.of(role);
        when(roleRepository.findByRoleNameContainingIgnoreCase(roleName)).thenReturn(mockRoles);

        // Act
        List<Role> result = searchRoleByNameService.searchRolesByName(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getRoleName());
    }

    @Test
    void testSearchRolesByName_NotFound() {
        // Arrange
        String roleName = "nonexistent";
        when(roleRepository.findByRoleNameContainingIgnoreCase(roleName)).thenReturn(Collections.emptyList());

        // Act & Assert
        RoleNameNotFoundException exception = assertThrows(
                RoleNameNotFoundException.class,
                () -> searchRoleByNameService.searchRolesByName(roleName)
        );

        assertEquals("No Roles found with name containing: nonexistent", exception.getMessage());
    }
}