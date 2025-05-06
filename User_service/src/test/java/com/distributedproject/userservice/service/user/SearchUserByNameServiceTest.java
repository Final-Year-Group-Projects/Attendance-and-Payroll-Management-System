package com.distributedproject.userservice.service.user;

import com.distributedproject.userservice.exception.user.UserNameNotFoundException;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchUserByNameServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchUserByNameService searchUserByNameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchUsersByName_shouldReturnUsersWhenFound() {
        // Arrange: Creating mock users
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUserFullName("Alice Smith");
        user1.setUserType("Admin");
        user1.setUserAddress("123 Main St");
        user1.setUserTelephone("123-456-7890");
        user1.setDepartmentId(1L);
        user1.setRoleId(1L);

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUserFullName("Alice Johnson");
        user2.setUserType("User");
        user2.setUserAddress("456 Elm St");
        user2.setUserTelephone("098-765-4321");
        user2.setDepartmentId(2L);
        user2.setRoleId(2L);

        List<User> mockUsers = Arrays.asList(user1, user2);

        // Mocking repository call
        when(userRepository.findByUserFullNameContainingIgnoreCase("Alice")).thenReturn(mockUsers);

        // Act: Calling the service method
        List<User> result = searchUserByNameService.searchUsersByName("Alice");

        // Assert: Validating the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice Smith", result.get(0).getUserFullName());
        assertEquals("Alice Johnson", result.get(1).getUserFullName());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUserFullNameContainingIgnoreCase("Alice");
    }

    @Test
    void searchUsersByName_shouldThrowUserNameNotFoundExceptionWhenNoUsersFound() {
        // Arrange: Mocking repository to return empty list when no users are found
        when(userRepository.findByUserFullNameContainingIgnoreCase("NonExistentName"))
                .thenReturn(Arrays.asList());

        // Act & Assert: Calling the service method and expecting exception
        UserNameNotFoundException exception = assertThrows(
                UserNameNotFoundException.class,
                () -> searchUserByNameService.searchUsersByName("NonExistentName")
        );

        // Assert: Validating exception message
        assertEquals("No users found with name containing: NonExistentName", exception.getMessage());

        // Verify interaction with the repository
        verify(userRepository, times(1)).findByUserFullNameContainingIgnoreCase("NonExistentName");
    }
}
