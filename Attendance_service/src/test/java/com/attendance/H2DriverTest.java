package com.attendance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class H2DriverTest {

    @Test
    void testH2DriverAvailability() {
        assertDoesNotThrow(() -> Class.forName("org.h2.Driver"));
    }
}