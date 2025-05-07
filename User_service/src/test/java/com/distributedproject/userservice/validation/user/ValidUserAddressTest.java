package com.distributedproject.userservice.validation.user;

import com.distributedproject.userservice.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidUserAddressTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private User buildUserWithAddress(String address) {
        User user = new User();
        user.setUserId("U123");
        user.setUserFullName("John Doe");
        user.setUserType("Admin");
        user.setUserTelephone("1234567890");
        user.setDepartmentId(1L);
        user.setRoleId(1L);
        user.setUserAddress(address);
        return user;
    }

    @Test
    public void testValidAddress() {
        User user = buildUserWithAddress("123 Main Street");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Expected no validation errors for valid address");
    }

    @Test
    public void testBlankAddress() {
        User user = buildUserWithAddress("   ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Expected validation error for blank address");

        boolean hasAddressError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("userAddress"));
        assertTrue(hasAddressError, "Expected error on 'userAddress' field");
    }

    @Test
    public void testNullAddress() {
        User user = buildUserWithAddress(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Expected no validation errors for null address");
    }
}
