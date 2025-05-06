package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.CreateDepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateDepartmentController {

    @Autowired
    private CreateDepartmentService departmentService;

    @PostMapping("/create/departments")
    public Department createDepartment(@Valid @RequestBody Department department) {
        System.out.println("Received department: " + department);
        return departmentService.createDepartment(department);
    }
}
