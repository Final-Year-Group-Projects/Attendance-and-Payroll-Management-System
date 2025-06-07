package com.distributedproject.userservice.validation.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleDescriptionValidatorTest {

    private RoleDescriptionValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new RoleDescriptionValidator();
    }

    @Test
    public void testValidDescriptions() {
        assertTrue(validator.isValid("Responsible for managing team projects.", null));
        assertTrue(validator.isValid("Leads the technical team with strategic decisions.", null));
        assertTrue(validator.isValid("Intern involved in research and development.", null));
        // 10 characters edge case
        assertTrue(validator.isValid("1234567890", null));
        // 255 characters edge case
        String longValidDescription = "a".repeat(255);
        assertTrue(validator.isValid(longValidDescription, null));
    }

    @Test
    public void testInvalidDescriptions() {
        // null input
        assertFalse(validator.isValid(null, null));
        // less than 10 characters
        assertFalse(validator.isValid("Too short", null));
        // exactly 9 characters
        assertFalse(validator.isValid("123456789", null));
        // more than 255 characters
        String tooLongDescription = "a".repeat(256);
        assertFalse(validator.isValid(tooLongDescription, null));
    }
}
