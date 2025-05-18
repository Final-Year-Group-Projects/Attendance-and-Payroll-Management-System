package com.distributedproject.userservice.service.roles;

import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GetAllRolesServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private GetAllRolesService getAllRolesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles() {
        // Arrange
        Role role1 = new Role();
        role1.setRoleId(1L);
        role1.setRoleName("ADMIN");

        Role role2 = new Role();
        role2.setRoleId(2L);
        role2.setRoleName("USER");

        List<Role> expectedRoles = Arrays.asList(role1, role2);

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        // Act
        List<Role> actualRoles = getAllRolesService.getAllRoles();

        // Assert
        assertEquals(expectedRoles, actualRoles);
    }
}
