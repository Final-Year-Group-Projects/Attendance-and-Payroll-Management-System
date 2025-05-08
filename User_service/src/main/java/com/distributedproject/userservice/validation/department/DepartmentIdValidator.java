package com.distributedproject.userservice.validation.department;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DepartmentIdValidator implements ConstraintValidator<ValidDepartmentId, Long> {

    @Override
    public boolean isValid(Long departmentId, ConstraintValidatorContext context) {
        // Example validation logic: ID must be positive and not null
        return departmentId == null || departmentId > 0;
    }
}
