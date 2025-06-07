package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNameNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchDepartmentByNameServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private SearchDepartmentByNameService searchDepartmentByNameService;

    @Test
    void searchDepartmentsByName_WhenDepartmentsFound_ShouldReturnList() {
        // Arrange
        String name = "Engineering";
        Department department = new Department(
                1L,
                "Engineering",
                "Alice",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(departmentRepository.findByDepartmentNameContainingIgnoreCase(name))
                .thenReturn(List.of(department));

        // Act
        List<Department> result = searchDepartmentByNameService.searchDepartmentsByName(name);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Engineering", result.get(0).getDepartmentName());
        verify(departmentRepository, times(1)).findByDepartmentNameContainingIgnoreCase(name);
    }

    @Test
    void searchDepartmentsByName_WhenNoDepartmentsFound_ShouldThrowException() {
        // Arrange
        String name = "NonExistentDept";
        when(departmentRepository.findByDepartmentNameContainingIgnoreCase(name))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(DepartmentNameNotFoundException.class, () ->
                searchDepartmentByNameService.searchDepartmentsByName(name));
        verify(departmentRepository, times(1)).findByDepartmentNameContainingIgnoreCase(name);
    }
}
