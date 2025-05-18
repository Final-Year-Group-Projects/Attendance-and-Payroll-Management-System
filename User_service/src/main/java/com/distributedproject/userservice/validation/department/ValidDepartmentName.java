package com.distributedproject.userservice.validation.department;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartmentNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDepartmentName {
    String message() default "Department Name must be one of 'Human Resources', 'Finance', 'Quality Assurance', 'Sales and Marketing', 'Customer Support'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
