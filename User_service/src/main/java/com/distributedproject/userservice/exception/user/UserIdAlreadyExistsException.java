package com.distributedproject.userservice.exception.user;

public class UserIdAlreadyExistsException extends RuntimeException {
    public UserIdAlreadyExistsException(String name) {
        super("User name is already taken.");
    }
}
