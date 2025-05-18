package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserRoleNotFoundException;
import com.distributedproject.userservice.model.Role;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.RoleRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetUserRoleByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private GetUserRoleByIdService getUserRoleByIdService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId("user123");
        user.setRoleId(1L); // Set a mock role ID for the user
    }

    @Test
    void testGetUserRoleById_Success() {
        // Arrange
        when(userRepository.findByUserId("user123")).thenReturn(Optional.of(user));

        // Create Role object using setter methods instead of constructor
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ADMIN");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Act
        Optional<String> roleName = getUserRoleByIdService.getUserRoleById("user123");

        // Assert
        assertTrue(roleName.isPresent());
        assertEquals("ADMIN", roleName.get());
    }

    @Test
    void testGetUserRoleById_UserNotFound() {
        // Arrange
        when(userRepository.findByUserId("user123")).thenReturn(Optional.empty());

        // Act
        Optional<String> roleName = getUserRoleByIdService.getUserRoleById("user123");

        // Assert
        assertFalse(roleName.isPresent());
    }

    @Test
    void testGetUserRoleById_RoleNotFound() {
        // Arrange
        user.setRoleId(null);  // Simulate no role assigned to the user
        when(userRepository.findByUserId("user123")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserRoleNotFoundException.class, () -> {
            getUserRoleByIdService.getUserRoleById("user123");
        });
    }

    @Test
    void testGetUserRoleById_RoleIdNotFound() {
        // Arrange
        user.setRoleId(2L);  // A roleId that does not exist in the roleRepository
        when(userRepository.findByUserId("user123")).thenReturn(Optional.of(user));
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Optional<String> roleName = getUserRoleByIdService.getUserRoleById("user123");

        // Assert
        assertFalse(roleName.isPresent());  // Assert that the result is an empty Optional when the role is not found
    }
}
