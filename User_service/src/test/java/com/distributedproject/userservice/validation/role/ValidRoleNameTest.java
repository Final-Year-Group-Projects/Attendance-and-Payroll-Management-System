package com.distributedproject.userservice.validation.role;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidRoleNameAnnotationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class TestRole {
        @ValidRoleName
        private String roleName;

        public TestRole(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }

    @Test
    void testValidRoleNameAnnotation() {
        TestRole role = new TestRole("Manager");
        Set<ConstraintViolation<TestRole>> violations = validator.validate(role);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidRoleNameAnnotation() {
        TestRole role = new TestRole("CEO");
        Set<ConstraintViolation<TestRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
        assertEquals("Role Name must be one of 'Manager', 'Tech Lead', 'Intern', 'HR', or 'Finance'",
                violations.iterator().next().getMessage());
    }

    @Test
    void testNullRoleName() {
        TestRole role = new TestRole(null);
        Set<ConstraintViolation<TestRole>> violations = validator.validate(role);
        assertFalse(violations.isEmpty());
    }
}
