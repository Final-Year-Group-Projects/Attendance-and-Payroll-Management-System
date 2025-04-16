package com.distributedproject.userservice.exception;

public class UserNameNotFoundException extends RuntimeException {
    public UserNameNotFoundException(String name) {
        super("No users found with name containing: " + name);
    }
}
