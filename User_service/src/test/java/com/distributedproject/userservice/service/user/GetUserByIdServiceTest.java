package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdService getUserByIdService;

    private User createSampleUser() {
        User user = new User();
        user.setUserId("E101"); // String userId
        user.setUserFullName("Alice Smith");
        user.setUserType("Admin");
        user.setUserAddress("123 Main St");
        user.setUserTelephone("123-456-7890");
        user.setDepartmentId(1L);
        user.setRoleId(1L);
        return user;
    }

    @Test
    void getUserById_shouldReturnUserWhenFound() {
        // Arrange
        User user = createSampleUser();
        when(userRepository.findByUserId("E101")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = getUserByIdService.getUserById("E101");

        // Assert
        assertAll("User fields",
                () -> assertTrue(result.isPresent(), "User should be present"),
                () -> assertEquals("Alice Smith", result.get().getUserFullName()),
                () -> assertEquals("E101", result.get().getUserId())
        );

        verify(userRepository, times(1)).findByUserId("E101");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_shouldReturnEmptyOptionalWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUserId("E999")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = getUserByIdService.getUserById("E999");

        // Assert
        assertFalse(result.isPresent(), "User should not be found");

        verify(userRepository, times(1)).findByUserId("E999");
        verifyNoMoreInteractions(userRepository);
    }
}
