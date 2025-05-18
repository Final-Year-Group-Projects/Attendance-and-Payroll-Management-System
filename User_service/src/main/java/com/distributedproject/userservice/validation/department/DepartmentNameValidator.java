package com.distributedproject.userservice.validation.department;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class DepartmentNameValidator implements ConstraintValidator<ValidDepartmentName, String> {

    private static final Set<String> VALID_DEPARTMENT_TYPES = Set.of("Human Resources", "Finance", "Quality Assurance", "Sales and Marketing", "Customer Support");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID_DEPARTMENT_TYPES.contains(value);
    }
}
