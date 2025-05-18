package com.distributedproject.userservice.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserType {
    String message() default "User type must be either 'Admin' 'Super_Admin' or 'Employee'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
