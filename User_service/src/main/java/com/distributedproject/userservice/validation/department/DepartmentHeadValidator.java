package com.distributedproject.userservice.validation.department;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DepartmentHeadValidator implements ConstraintValidator<ValidDepartmentHead, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^[A-Za-z ]+$");
    }
}
