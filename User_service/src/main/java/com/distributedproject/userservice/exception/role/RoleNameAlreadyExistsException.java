package com.distributedproject.userservice.exception.role;

public class RoleNameAlreadyExistsException extends RuntimeException {
    public RoleNameAlreadyExistsException(String name) {
        super("Role name is already taken.");
    }
}
