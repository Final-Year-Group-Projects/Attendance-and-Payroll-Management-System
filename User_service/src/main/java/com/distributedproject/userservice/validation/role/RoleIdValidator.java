package com.distributedproject.userservice.validation.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleIdValidator implements ConstraintValidator<ValidRoleId, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value != null && value > 0;
    }
}
