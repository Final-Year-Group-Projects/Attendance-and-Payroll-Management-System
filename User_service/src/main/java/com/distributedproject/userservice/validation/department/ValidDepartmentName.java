package com.distributedproject.userservice.validation.department;

import com.distributedproject.userservice.validation.department.DepartmentNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartmentNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDepartmentName {
    String message() default "Department name must only contain letters and spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
