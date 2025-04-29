package com.distributedproject.userservice.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserAddressValidator implements ConstraintValidator<ValidUserAddress, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Return true if value is null, else check if it's not empty or whitespace
        return value == null || !value.trim().isEmpty();
    }
}
