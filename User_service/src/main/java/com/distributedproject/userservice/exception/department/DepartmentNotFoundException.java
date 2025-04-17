package com.distributedproject.userservice.exception.department;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super("Department with ID " + id + " not found");
    }
}
