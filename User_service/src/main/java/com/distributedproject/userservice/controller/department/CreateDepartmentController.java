package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.CreateDepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CreateDepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(CreateDepartmentController.class);

    @Autowired
    private CreateDepartmentService departmentService;

    @PostMapping("/user/create/departments")
    public Department createDepartment(@Valid @RequestBody Department department) {
        logger.info("Received department creation request for department: {}", department);

        Department createdDepartment = departmentService.createDepartment(department);

        logger.info("Department created successfully: {}", createdDepartment);

        return createdDepartment;
    }
}
