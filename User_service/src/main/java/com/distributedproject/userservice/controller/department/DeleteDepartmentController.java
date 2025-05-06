package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.service.department.DeleteDepartmentService;
import com.distributedproject.userservice.service.user.DeleteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeleteDepartmentController {

    @Autowired
    private DeleteDepartmentService departmentService;

    @DeleteMapping("/delete/departments/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("Department deleted successfully");
    }
}
