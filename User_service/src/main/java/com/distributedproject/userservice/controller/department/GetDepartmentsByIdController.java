package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.GetDepartmentsByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GetDepartmentsByIdController {

    private static final Logger logger = LoggerFactory.getLogger(GetDepartmentsByIdController.class);

    @Autowired
    private GetDepartmentsByIdService departmentService;

    @GetMapping("/user/get/departments/{departmentId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long departmentId) {
        // Log the received request with the department ID
        logger.info("Received request to fetch department with ID: {}", departmentId);

        Department department = departmentService.getUserById(departmentId)
                .orElseThrow(() -> {
                    // Log an error if the department is not found
                    logger.error("Department with ID {} not found", departmentId);
                    return new DepartmentNotFoundException(departmentId);
                });

        // Log the successful response
        logger.info("Successfully fetched department with ID: {}", departmentId);

        return ResponseEntity.ok(department);
    }
}
