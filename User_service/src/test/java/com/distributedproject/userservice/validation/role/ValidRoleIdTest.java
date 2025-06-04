package com.distributedproject.userservice.validation.role;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidRoleIdTest {

    private Validator validator;

    private static class DummyRole {
        @ValidRoleId
        private Long id;

        public DummyRole(Long id) {
            this.id = id;
        }
    }

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidRoleId() {
        DummyRole role = new DummyRole(5L);
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidRoleId_Null() {
        DummyRole role = new DummyRole(null);
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
        assertEquals("Invalid role ID. Must be a positive number.", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidRoleId_Zero() {
        DummyRole role = new DummyRole(0L);
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidRoleId_Negative() {
        DummyRole role = new DummyRole(-10L);
        Set<ConstraintViolation<DummyRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
    }
}
