package com.app.security.dto;

/**
 * Payload d'inscription utilisateur.
 */
public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
