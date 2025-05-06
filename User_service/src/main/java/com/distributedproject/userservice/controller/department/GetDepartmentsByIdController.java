package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.exception.department.DepartmentNotFoundException;
import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.GetDepartmentsByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GetDepartmentsByIdController {

    @Autowired
    private GetDepartmentsByIdService departmentService;

    @GetMapping("/get/departments/{departmentId}")
    public ResponseEntity<Department> getUserById(@PathVariable Long departmentId) {
        Department department = departmentService.getUserById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        return ResponseEntity.ok(department);
    }
}
