package com.distributedproject.authservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBlacklistRepositoryTest {

    private TokenBlacklistRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TokenBlacklistRepository();
    }

    @Test
    void testTokenIsBlacklistedAfterBlacklisting() {
        String token = "blacklisted.token";

        repository.blacklist(token);

        assertTrue(repository.isBlacklisted(token));
    }

    @Test
    void testTokenIsNotBlacklistedIfNotAdded() {
        String token = "nonblacklisted.token";

        assertFalse(repository.isBlacklisted(token));
    }

    @Test
    void testMultipleTokensBlacklistedCorrectly() {
        String token1 = "token.1";
        String token2 = "token.2";

        repository.blacklist(token1);
        repository.blacklist(token2);

        assertTrue(repository.isBlacklisted(token1));
        assertTrue(repository.isBlacklisted(token2));
        assertFalse(repository.isBlacklisted("token.3"));
    }
}
