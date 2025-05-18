package com.distributedproject.userservice.validation.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAddressValidatorTest {

    private UserAddressValidator userAddressValidator;

    @BeforeEach
    void setUp() {
        userAddressValidator = new UserAddressValidator();
    }

    @Test
    void testValidAddress() {
        assertTrue(userAddressValidator.isValid("123 Main St", null));
    }

    @Test
    void testEmptyAddress() {
        assertFalse(userAddressValidator.isValid("", null));
    }

    @Test
    void testNullAddress() {
        assertTrue(userAddressValidator.isValid(null, null));
    }

    @Test
    void testAddressWithWhitespace() {
        assertFalse(userAddressValidator.isValid("   ", null));
    }

    @Test
    void testValidAddressWithWhitespace() {
        assertTrue(userAddressValidator.isValid(" 123 Main St ", null));
    }
}
