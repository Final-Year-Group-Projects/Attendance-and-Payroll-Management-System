package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteDepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DeleteDepartmentService deleteDepartmentService;

    @Test
    void deleteDepartment_WhenDepartmentExists_ShouldDeleteDepartment() {
        Long departmentId = 1L;
        when(departmentRepository.existsById(departmentId)).thenReturn(true);

        deleteDepartmentService.deleteDepartment(departmentId);

        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    @Test
    void deleteDepartment_WhenDepartmentDoesNotExist_ShouldThrowDepartmentNotFoundException() {
        Long departmentId = 2L;
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class, () -> {
            deleteDepartmentService.deleteDepartment(departmentId);
        });

        assertEquals("Department with ID 2 not found", exception.getMessage());
        verify(departmentRepository, never()).deleteById(departmentId);
    }
}
