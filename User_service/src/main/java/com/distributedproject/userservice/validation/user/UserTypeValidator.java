package com.distributedproject.userservice.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class UserTypeValidator implements ConstraintValidator<ValidUserType, String> {

    private static final Set<String> VALID_USER_TYPES = Set.of("admin", "employee");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID_USER_TYPES.contains(value.toLowerCase());
    }
}
