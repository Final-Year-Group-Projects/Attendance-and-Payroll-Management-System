package com.distributedproject.userservice.service.department;

import com.distributedproject.userservice.exception.department.DepartmentNameNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchDepartmentByNameService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> searchDepartmentsByName(String name) {
        List<Department> departments = departmentRepository.findByDepartmentNameContainingIgnoreCase(name);
        if (departments.isEmpty()) {
            throw new DepartmentNameNotFoundException(name);
        }
        return departments;
    }
}
