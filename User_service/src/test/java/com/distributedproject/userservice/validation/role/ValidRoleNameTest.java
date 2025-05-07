package com.distributedproject.userservice.validation.role;

import com.distributedproject.userservice.model.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidRoleNameTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRoleName() {
        Role role = new Role();
        role.setRoleName("Manager");

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertTrue(violations.isEmpty(), "Expected no validation errors for valid role name");
    }

    @Test
    void testInvalidRoleName() {
        Role role = new Role();
        role.setRoleName("CEO");

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertFalse(violations.isEmpty(), "Expected validation errors for invalid role name");

        boolean hasRoleNameError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("roleName"));
        assertTrue(hasRoleNameError, "Expected violation on 'roleName' field");
    }

    @Test
    void testNullRoleName() {
        Role role = new Role();
        role.setRoleName(null);

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertFalse(violations.isEmpty(), "Expected validation error for null role name");
    }
}

