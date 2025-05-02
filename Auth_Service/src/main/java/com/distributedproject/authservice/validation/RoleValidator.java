package com.distributedproject.authservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    private static final Set<String> VALID_ROLES = Set.of("user", "employee");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID_ROLES.contains(value.toLowerCase());
    }
}
