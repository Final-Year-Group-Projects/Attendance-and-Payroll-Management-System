package com.distributedproject.userservice.validation.department;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartmentHeadValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDepartmentHead {
    String message() default "Department head must only contain letters and spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
