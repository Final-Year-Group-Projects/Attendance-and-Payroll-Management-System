package com.distributedproject.userservice.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserId {
    String message() default "User ID must start with E, S, or M followed by digits (e.g., E101, S228)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
