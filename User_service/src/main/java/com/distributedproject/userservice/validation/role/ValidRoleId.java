package com.distributedproject.userservice.validation.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoleId {
    String message() default "Invalid role ID. Must be a positive number.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
