package com.distributedproject.userservice.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserTelephoneValidator implements ConstraintValidator<ValidUserTelephone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^\\d{10}$");
    }
}
