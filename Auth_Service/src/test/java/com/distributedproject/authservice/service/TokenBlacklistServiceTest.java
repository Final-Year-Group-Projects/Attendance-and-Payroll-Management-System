package com.distributedproject.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBlacklistServiceTest {

    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    void setUp() {
        tokenBlacklistService = new TokenBlacklistService();
    }

    @Test
    void testTokenIsBlacklistedAfterAdding() {
        String token = "sample.jwt.token";

        // Act
        tokenBlacklistService.blacklistToken(token);

        // Assert
        assertTrue(tokenBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    void testTokenIsNotBlacklistedIfNotAdded() {
        String token = "untracked.jwt.token";

        // Assert
        assertFalse(tokenBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    void testMultipleTokensBlacklisting() {
        String token1 = "token.one";
        String token2 = "token.two";

        tokenBlacklistService.blacklistToken(token1);
        tokenBlacklistService.blacklistToken(token2);

        assertTrue(tokenBlacklistService.isTokenBlacklisted(token1));
        assertTrue(tokenBlacklistService.isTokenBlacklisted(token2));
        assertFalse(tokenBlacklistService.isTokenBlacklisted("token.three"));
    }
}
