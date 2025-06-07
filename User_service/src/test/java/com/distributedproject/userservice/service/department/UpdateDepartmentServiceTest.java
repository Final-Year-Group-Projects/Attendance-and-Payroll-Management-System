package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateDepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private UpdateDepartmentService updateDepartmentService;

    @Test
    void updateDepartment_WhenDepartmentExistsAndNameIsUnique_ShouldUpdateSuccessfully() {
        // Arrange
        Long departmentId = 1L;
        Department existing = new Department(departmentId, "Finance", "Alice", LocalDateTime.now(), LocalDateTime.now());
        Department updatedDetails = new Department(null, "HR", "Bob", null, null);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(existing));
        when(departmentRepository.findByDepartmentNameIgnoreCase("HR")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Department updated = updateDepartmentService.updateDepartment(departmentId, updatedDetails);

        // Assert
        assertEquals("HR", updated.getDepartmentName());
        assertEquals("Bob", updated.getDepartmentHead());
        verify(departmentRepository, times(1)).save(existing);
    }

    @Test
    void updateDepartment_WhenDepartmentNotFound_ShouldThrowNotFoundException() {
        // Arrange
        Long departmentId = 99L;
        Department dummyDetails = new Department(null, "NewDept", "John", null, null);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DepartmentNotFoundException.class,
                () -> updateDepartmentService.updateDepartment(departmentId, dummyDetails));
    }

    @Test
    void updateDepartment_WhenNameAlreadyExists_ShouldThrowAlreadyExistsException() {
        // Arrange
        Long departmentId = 1L;
        Department existing = new Department(departmentId, "Finance", "Alice", LocalDateTime.now(), LocalDateTime.now());
        Department updatedDetails = new Department(null, "HR", "Bob", null, null);
        Department conflicting = new Department(2L, "HR", "Charlie", LocalDateTime.now(), LocalDateTime.now());

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(existing));
        when(departmentRepository.findByDepartmentNameIgnoreCase("HR")).thenReturn(Optional.of(conflicting));

        // Act & Assert
        assertThrows(DepartmentNameAlreadyExistsException.class,
                () -> updateDepartmentService.updateDepartment(departmentId, updatedDetails));
    }
}
