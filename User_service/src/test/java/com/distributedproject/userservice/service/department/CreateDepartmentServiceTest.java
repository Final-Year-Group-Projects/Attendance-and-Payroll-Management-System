package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateDepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private CreateDepartmentService createDepartmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDepartment_Success() {
        Department department = new Department();
        department.setDepartmentName("IT");

        when(departmentRepository.existsByDepartmentNameIgnoreCase("IT")).thenReturn(false);
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = createDepartmentService.createDepartment(department);

        assertEquals("IT", result.getDepartmentName());
        verify(departmentRepository).save(department);
    }

    @Test
    void testCreateDepartment_NameAlreadyExists() {
        Department department = new Department();
        department.setDepartmentName("HR");

        when(departmentRepository.existsByDepartmentNameIgnoreCase("HR")).thenReturn(true);

        DepartmentNameAlreadyExistsException exception = assertThrows(
                DepartmentNameAlreadyExistsException.class,
                () -> createDepartmentService.createDepartment(department)
        );

        assertEquals("Department name is already taken.", exception.getMessage());
        verify(departmentRepository, never()).save(any());
    }
}
