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
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GetAllDepartmentsServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private GetAllDepartmentsService getAllDepartmentsService;

    @Test
    void getAllDepartments_ShouldReturnListOfDepartments() {
        // Arrange
        Department dept1 = new Department(
                1L, "Engineering", "Alice Johnson",
                LocalDateTime.now(), LocalDateTime.now()
        );
        Department dept2 = new Department(
                2L, "Marketing", "Bob Smith",
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<Department> mockDepartments = Arrays.asList(dept1, dept2);

        when(departmentRepository.findAll()).thenReturn(mockDepartments);

        // Act
        List<Department> result = getAllDepartmentsService.getAllDepartments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Engineering", result.get(0).getDepartmentName());
        assertEquals("Bob Smith", result.get(1).getDepartmentHead());
        verify(departmentRepository, times(1)).findAll();
    }
}
