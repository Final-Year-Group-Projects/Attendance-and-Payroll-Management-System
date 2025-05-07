package com.distributedproject.userservice.validation.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleDescriptionValidator implements ConstraintValidator<ValidRoleDescription, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() >= 10 && value.length() <= 255;
    }
}
