package com.distributedproject.userservice.exception.user;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String userId) {
        super("Role not found for user with ID: " + userId);
    }
}