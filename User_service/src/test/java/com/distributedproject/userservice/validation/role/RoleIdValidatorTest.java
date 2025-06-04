package com.distributedproject.userservice.validation.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleIdValidatorTest {

    private RoleIdValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new RoleIdValidator();
    }

    @Test
    public void testValidRoleIds() {
        assertTrue(validator.isValid(1L, null));
        assertTrue(validator.isValid(100L, null));
        assertTrue(validator.isValid(Long.MAX_VALUE, null));
    }

    @Test
    public void testInvalidRoleIds() {
        assertFalse(validator.isValid(null, null));   // null should be invalid
        assertFalse(validator.isValid(0L, null));     // zero is invalid
        assertFalse(validator.isValid(-1L, null));    // negative is invalid
        assertFalse(validator.isValid(-100L, null));  // large negative
    }
}
