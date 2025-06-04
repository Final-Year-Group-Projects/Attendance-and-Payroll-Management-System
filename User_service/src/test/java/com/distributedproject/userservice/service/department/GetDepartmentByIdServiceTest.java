package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GetDepartmentsByIdServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private GetDepartmentsByIdService getDepartmentsByIdService;

    @Test
    void getUserById_WhenDepartmentExists_ShouldReturnDepartment() {
        // Arrange
        Long departmentId = 1L;
        Department department = new Department(
                departmentId,
                "IT",
                "John Doe",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // Act
        Optional<Department> result = getDepartmentsByIdService.getUserById(departmentId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("IT", result.get().getDepartmentName());
        assertEquals("John Doe", result.get().getDepartmentHead());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void getUserById_WhenDepartmentDoesNotExist_ShouldReturnEmptyOptional() {
        // Arrange
        Long departmentId = 99L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Act
        Optional<Department> result = getDepartmentsByIdService.getUserById(departmentId);

        // Assert
        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
    }
}
