package com.distributedproject.userservice.exception.department;

public class UserDepartmentNotFoundException extends RuntimeException {
    public UserDepartmentNotFoundException(String userId) {
        super("Department not found for user with ID: " + userId);
    }
}