package com.app.security.dto;

/**
 * Réponse d'authentification contenant le jeton JWT et les informations utilisateur de base.
 */
public record AuthResponse(
        String token,
        String tokenType,
        Long id,
        String username,
        String email,
        String role
) {
}
