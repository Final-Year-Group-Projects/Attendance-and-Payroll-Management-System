package com.distributedproject.userservice.validation.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleDescriptionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoleDescription {
    String message() default "Role description must be between 10 and 255 characters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
