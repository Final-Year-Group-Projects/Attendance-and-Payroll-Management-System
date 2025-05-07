package com.distributedproject.userservice.validation.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserAddressValidatorTest {

    private UserAddressValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UserAddressValidator();
    }

    @Test
    public void testValidAddress() {
        assertTrue(validator.isValid("123 Main St", null));
    }

    @Test
    public void testEmptyAddress() {
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    public void testNullAddress() {
        assertTrue(validator.isValid(null, null));
    }
}
