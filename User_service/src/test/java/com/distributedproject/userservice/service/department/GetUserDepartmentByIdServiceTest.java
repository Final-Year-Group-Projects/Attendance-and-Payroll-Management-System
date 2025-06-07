package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.UserDepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.model.User;
import com.distributedproject.userservice.repository.DepartmentRepository;
import com.distributedproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserDepartmentByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private GetUserDepartmentByIdService getUserDepartmentByIdService;

    @Test
    void getUserDepartmentById_WhenUserAndDepartmentExist_ShouldReturnDepartmentName() {
        // Arrange
        String userId = "user123";
        Long departmentId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setDepartmentId(departmentId);

        Department department = new Department(
                departmentId,
                "Engineering",
                "Alice",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // Act
        Optional<String> result = getUserDepartmentByIdService.getUserDepartmentById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Engineering", result.get());
        verify(userRepository, times(1)).findByUserId(userId);
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void getUserDepartmentById_WhenUserExistsButDepartmentIdIsNull_ShouldThrowException() {
        // Arrange
        String userId = "user123";
        User user = new User();
        user.setUserId(userId);
        user.setDepartmentId(null);  // No department assigned

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserDepartmentNotFoundException.class, () ->
                getUserDepartmentByIdService.getUserDepartmentById(userId));

        verify(userRepository, times(1)).findByUserId(userId);
        verify(departmentRepository, never()).findById(any());
    }

    @Test
    void getUserDepartmentById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        String userId = "nonexistent";
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        Optional<String> result = getUserDepartmentByIdService.getUserDepartmentById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUserId(userId);
        verify(departmentRepository, never()).findById(any());
    }
}
