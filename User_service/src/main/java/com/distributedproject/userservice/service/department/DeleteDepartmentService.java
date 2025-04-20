package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public void deleteDepartment(Long departmentId) {
        if (departmentRepository.existsById(String.valueOf(departmentId))) {
            departmentRepository.deleteById(String.valueOf(departmentId));
        } else {
            throw new DepartmentNotFoundException(departmentId);
        }
    }
}
