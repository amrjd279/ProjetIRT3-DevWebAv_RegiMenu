package com.app.security.service;

import com.app.security.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitaires de l'utilitaire JwtService (1/3 implémenté, 2/3 à compléter par l'étudiant).
 */
class JwtServiceTest {

    private static final String TEST_SECRET = "RegiMenuTestSecretKeyForJwtTestsAtLeast256BitsLong123456";
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET, 3_600_000L);
    }

    @Test
    void shouldGenerateAndValidateJwtToken() {
        SecurityUser user = new SecurityUser(1L, "bastien", "bastien@regimenu.com", "hashed", "ROLE_USER");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("bastien", jwtService.extractUsername(token));
        assertTrue(jwtService.validateToken(token, user));
    }

    // =========================================================================
    // TODO: ÉTUDIANT (Test 2/3) — Vérifier qu'un jeton est refusé pour un autre utilisateur
    // Indice :
    // 1. Créer deux SecurityUser différents (userA et userB).
    // 2. Générer un token pour userA.
    // 3. Vérifier avec assertFalse(jwtService.validateToken(token, userB)).
    // N'oubliez pas d'ajouter @Test au-dessus de la méthode !
    // =========================================================================
    @Test
    void shouldRejectTokenForAnotherUser() {
        SecurityUser userA = new SecurityUser(1L, "bastien", "bastien@regimenu.com", "hashed", "ROLE_USER");
        SecurityUser userB = new SecurityUser(2L, "alice", "alice@regimenu.com", "hashed", "ROLE_USER");

        String token = jwtService.generateToken(userA);
        assertFalse(jwtService.validateToken(token, userB));
    }


    // =========================================================================
    // TODO: ÉTUDIANT (Test 3/3) — Vérifier qu'un jeton expiré lève ExpiredJwtException
    // Indice :
    // 1. Instancier un JwtService avec une expiration négative ou nulle (ex: -1000L).
    // 2. Générer un token pour un SecurityUser.
    // 3. Vérifier avec assertThrows(io.jsonwebtoken.ExpiredJwtException.class, ...)
    //    lors de l'appel à jwtService.extractUsername(token) ou validateToken(token, user).
    // =========================================================================
    @Test
    void shouldRejectExpiredToken() {
        JwtService expiredJwtService = new JwtService(TEST_SECRET, -1000L);
        SecurityUser user = new SecurityUser(1L, "bastien", "bastien@regimenu.com", "hashed", "ROLE_USER");
        String token = expiredJwtService.generateToken(user);
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> expiredJwtService.extractUsername(token));
    }

}
