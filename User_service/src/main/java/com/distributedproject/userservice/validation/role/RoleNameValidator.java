package com.distributedproject.userservice.validation.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RoleNameValidator implements ConstraintValidator<ValidRoleName, String> {

    private static final Set<String> VALID_ROLE_TYPES = Set.of("Manager", "Tech Lead", "Employee", "Intern", "Admin", "HR", "Finance");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID_ROLE_TYPES.contains(value);
    }
}
