package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetUserByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdService getUserByIdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_shouldReturnUserWhenFound() {
        // Arrange: Creating a mock user
        User user = new User();
        user.setUserId(1L);
        user.setUserFullName("Alice Smith");
        user.setUserType("Admin");
        user.setUserAddress("123 Main St");
        user.setUserTelephone("123-456-7890");
        user.setDepartmentId(1L);
        user.setRoleId(1L);

        // Mocking repository call
        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(user));

        // Act: Calling the service method
        Optional<User> result = getUserByIdService.getUserById(1L);

        // Assert: Validating the result
        assertTrue(result.isPresent());
        assertEquals("Alice Smith", result.get().getUserFullName());
        assertEquals(1L, result.get().getUserId());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserById_shouldReturnEmptyOptionalWhenUserNotFound() {
        // Arrange: Mocking repository to return empty Optional when user not found
        when(userRepository.findByUserId(99L)).thenReturn(Optional.empty());

        // Act: Calling the service method
        Optional<User> result = getUserByIdService.getUserById(99L);

        // Assert: Validating the result
        assertFalse(result.isPresent());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUserId(99L);
    }
}
