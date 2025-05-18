package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNameAlreadyExistsException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CreateDepartmentService {

    @Autowired
    private DepartmentRepository DepartmentRepository;

    public Department createDepartment(Department department) {
        if (DepartmentRepository.existsByDepartmentNameIgnoreCase(department.getDepartmentName())) {
            throw new DepartmentNameAlreadyExistsException(department.getDepartmentName());
        }

        return DepartmentRepository.save(department);
    }
}
