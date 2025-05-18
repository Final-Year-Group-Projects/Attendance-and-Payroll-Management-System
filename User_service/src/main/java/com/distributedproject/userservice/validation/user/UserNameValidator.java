package com.distributedproject.userservice.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<ValidUserName, String> {

    private static final String USERNAME_PATTERN = "^[A-Za-z]+( [A-Za-z]+)*$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        String trimmed = value.trim();
        return trimmed.matches(USERNAME_PATTERN);
    }
}
