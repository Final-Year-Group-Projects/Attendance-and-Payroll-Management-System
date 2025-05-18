package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GetRolesByIdServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private GetRolesByIdService getRolesByIdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRolesById_Found() {
        // Arrange
        Long roleId = 1L;
        Role expectedRole = new Role();
        expectedRole.setRoleId(roleId);
        expectedRole.setRoleName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(expectedRole));

        // Act
        Optional<Role> actualRole = getRolesByIdService.getRolesById(roleId);

        // Assert
        assertEquals(Optional.of(expectedRole), actualRole);
    }

    @Test
    void testGetRolesById_NotFound() {
        // Arrange
        Long roleId = 1L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act
        Optional<Role> actualRole = getRolesByIdService.getRolesById(roleId);

        // Assert
        assertEquals(Optional.empty(), actualRole);
    }
}
