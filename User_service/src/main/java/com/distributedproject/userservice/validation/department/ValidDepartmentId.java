package com.distributedproject.userservice.validation.department;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartmentIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDepartmentId {
    String message() default "Invalid Department ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
