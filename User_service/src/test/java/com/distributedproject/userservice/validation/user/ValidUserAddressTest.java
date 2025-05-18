package com.distributedproject.userservice.validation.user;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

class ValidUserAddressTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Create a ValidatorFactory and get the Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidAddress() {
        User user = new User("John Doe", "123 Main St");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "No validation errors should be found");
    }

    @Test
    void testInvalidEmptyAddress() {
        User user = new User("John Doe", "");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation error should be found for empty address");
        assertEquals("User address cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidWhitespaceAddress() {
        User user = new User("John Doe", "   ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation error should be found for whitespace-only address");
        assertEquals("User address cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullAddress() {
        User user = new User("John Doe", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "No validation errors should be found for null address");
    }

    // User class for validation testing
    static class User {
        private String name;

        @ValidUserAddress
        private String address;

        public User(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }
}
