package com.distributedproject.userservice.controller.department;

import com.distributedproject.userservice.model.Department;
import com.distributedproject.userservice.service.department.SearchDepartmentByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class SearchDepartmentsController {

    private static final Logger logger = LoggerFactory.getLogger(SearchDepartmentsController.class);

    @Autowired
    private SearchDepartmentByNameService departmentService;

    @GetMapping("/get/departments/search")
    public List<Department> searchDepartments(@RequestParam String name) {
        // Log the received request with the department name being searched
        logger.info("Received request to search departments by name: {}", name);

        List<Department> departments = departmentService.searchDepartmentsByName(name);

        // Log the successful response and the number of departments found
        logger.info("Successfully found {} department(s) matching the name '{}'", departments.size(), name);

        return departments;
    }
}
