package com.distributedproject.userservice.validation.role;

import com.distributedproject.userservice.validation.role.RoleNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleNameValidatorTest {

    private RoleNameValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new RoleNameValidator();
    }

    @Test
    public void testValidRoleNames() {
        assertTrue(validator.isValid("Manager", null));
        assertTrue(validator.isValid("Tech Lead", null));
        assertTrue(validator.isValid("Intern", null));
        assertTrue(validator.isValid("HR", null));
        assertTrue(validator.isValid("Finance", null));
    }

    @Test
    public void testInvalidRoleNames() {
        assertFalse(validator.isValid("CEO", null));
        assertFalse(validator.isValid("Developer", null));
        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("manager", null)); // case-sensitive check
        assertFalse(validator.isValid("TechLead", null)); // missing space
    }

    @Test
    public void testNullRoleName() {
        assertFalse(validator.isValid(null, null));
    }
}
