package com.distributedproject.userservice.exception.user;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String name) {
        super("User name is already taken.");
    }
}
