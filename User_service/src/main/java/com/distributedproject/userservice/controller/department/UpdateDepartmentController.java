package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.UpdateDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateDepartmentController {

    @Autowired
    private UpdateDepartmentService updateDepartmentService;  // Autowire the correct service class

    @PutMapping("/departments/{departmentId}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long departmentId, @RequestBody Department departmentDetails) {
        // Now we throw UserNotFoundException if the user doesn't exist
        Department updatedDepartment = updateDepartmentService.updateDepartment(departmentId, departmentDetails);

        // If user is null or not found, throw exception (UserNotFoundException)
        if (updatedDepartment == null) {
            throw new DepartmentNotFoundException(departmentId); // Trigger custom exception
        }

        // If user is found and updated
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }
}

