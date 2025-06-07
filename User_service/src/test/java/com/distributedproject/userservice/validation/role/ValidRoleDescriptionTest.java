package com.distributedproject.userservice.validation.role;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidRoleDescriptionTest {

    private Validator validator;

    private static class DummyRole {
        @ValidRoleDescription
        private String description;

        public DummyRole(String description) {
            this.description = description;
        }
    }

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDescription() {
        DummyRole role = new DummyRole("Handles team management and coordination.");
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidDescriptionTooShort() {
        DummyRole role = new DummyRole("Too short");
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
        assertEquals("Role description must be between 10 and 255 characters.", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidDescriptionTooLong() {
        String tooLong = "a".repeat(256);
        DummyRole role = new DummyRole(tooLong);
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNullDescription() {
        DummyRole role = new DummyRole(null);
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
    }
}
