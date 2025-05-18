package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.GetAllDepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class GetDepartmentsController {

    private static final Logger logger = LoggerFactory.getLogger(GetDepartmentsController.class);

    @Autowired
    private GetAllDepartmentsService departmentService;

    @GetMapping("/user/get/departments")
    public List<Department> getDepartments() {
        // Log the received request
        logger.info("Received request to fetch all departments.");

        List<Department> departments = departmentService.getAllDepartments();

        // Log the successful response
        logger.info("Successfully fetched all departments. Number of departments: {}", departments.size());

        return departments;
    }
}
