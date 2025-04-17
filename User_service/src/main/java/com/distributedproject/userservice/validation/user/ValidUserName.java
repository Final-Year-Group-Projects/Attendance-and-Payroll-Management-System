package com.distributedproject.userservice.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserName {
    String message() default "User name must only contain letters and spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
