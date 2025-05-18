package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.UpdateDepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UpdateDepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDepartmentController.class);

    @Autowired
    private UpdateDepartmentService updateDepartmentService;

    @PutMapping("/user/update/departments/{departmentId}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long departmentId, @Valid @RequestBody Department departmentDetails) {
        // Log the received request
        logger.info("Received request to update department with ID: {}", departmentId);

        Department updatedDepartment = updateDepartmentService.updateDepartment(departmentId, departmentDetails);

        // If department is null or not found, throw exception (DepartmentNotFoundException)
        if (updatedDepartment == null) {
            logger.error("Department with ID {} not found.", departmentId);
            throw new DepartmentNotFoundException(departmentId); // Trigger custom exception
        }

        // Log successful update
        logger.info("Successfully updated department with ID: {}", departmentId);

        // If department is found and updated
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }
}
