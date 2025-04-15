package com.distributedproject.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserTelephoneValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserTelephone {
    String message() default "Telephone must be a 10-digit number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
