package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetDepartmentsByIdService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Optional<Department> getUserById(Long departmentId) {
        return departmentRepository.findById(String.valueOf(departmentId));
    }
}
