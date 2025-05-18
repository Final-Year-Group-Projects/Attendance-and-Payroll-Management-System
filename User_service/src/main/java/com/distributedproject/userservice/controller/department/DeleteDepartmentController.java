package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.service.department.DeleteDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DeleteDepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DeleteDepartmentController.class);

    @Autowired
    private DeleteDepartmentService departmentService;

    @DeleteMapping("/delete/departments/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long departmentId) {
        logger.info("Received request to delete department with ID: {}", departmentId);

        departmentService.deleteDepartment(departmentId);

        logger.info("Department with ID: {} deleted successfully", departmentId);

        return ResponseEntity.ok("Department deleted successfully");
    }
}
