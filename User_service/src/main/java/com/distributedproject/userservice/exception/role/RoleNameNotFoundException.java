package com.distributedproject.userservice.exception.role;

public class RoleNameNotFoundException extends RuntimeException {
    public RoleNameNotFoundException(String name) {
        super("No Roles found with name containing: " + name);
    }
}
