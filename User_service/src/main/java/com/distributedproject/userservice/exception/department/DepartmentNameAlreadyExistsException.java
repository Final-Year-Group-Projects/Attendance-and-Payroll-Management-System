package com.distributedproject.userservice.exception.department;

public class DepartmentNameAlreadyExistsException extends RuntimeException {
    public DepartmentNameAlreadyExistsException(String name) {
        super("Department name is already taken.");
    }
}
