package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CreateDepartmentService {

    @Autowired
    private DepartmentRepository DepartmentRepository;

    public Department createDepartment(Department department) {
        if (DepartmentRepository.existsByDepartmentNameIgnoreCase(department.getDepartmentName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department name already taken.");
        }

        return DepartmentRepository.save(department);
    }
}
