package com.distributedproject.userservice.validation.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoleName {
    String message() default "Role Name must be one of 'Manager', 'Tech Lead', 'Employee', 'Intern', 'Admin', 'HR', or 'Finance'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
