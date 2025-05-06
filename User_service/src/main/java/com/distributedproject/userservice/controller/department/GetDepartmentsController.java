package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.GetAllDepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetDepartmentsController {

    @Autowired
    private GetAllDepartmentsService departmentService;

    @GetMapping("/get/departments")
    public List<Department> getDepartments() {
        return departmentService.getAllDepartments();
    }
}
