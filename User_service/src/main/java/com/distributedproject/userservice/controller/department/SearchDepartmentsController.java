package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.SearchDepartmentByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SearchDepartmentsController {

    @Autowired
    private SearchDepartmentByNameService departmentService;

    @GetMapping("/departments/search")
    public List<Department> searchUsers(@RequestParam String name) {
        return departmentService.searchDepartmentsByName(name);
    }
}
