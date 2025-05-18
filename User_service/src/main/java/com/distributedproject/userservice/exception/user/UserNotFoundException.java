package com.distributedproject.userservice.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User with ID " + id + " not found");
    }
}
