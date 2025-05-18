package com.distributedproject.userservice.exception.department;

public class DepartmentNameNotFoundException extends RuntimeException {
    public DepartmentNameNotFoundException(String name) {
        super("No departments found with name containing: " + name);
    }
}
