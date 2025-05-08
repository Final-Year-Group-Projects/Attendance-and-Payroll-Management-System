package com.distributedproject.userservice.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserIdValidator implements ConstraintValidator<ValidUserId, String> {

    private static final String USER_ID_PATTERN = "^[ESM]\\d+$";

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        return userId != null && userId.matches(USER_ID_PATTERN);
    }
}
