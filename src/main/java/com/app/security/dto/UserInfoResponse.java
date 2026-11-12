package com.app.security.dto;

/**
 * Informations publiques de l'utilisateur connecté retournées par {@code GET /api/auth/me}.
 */
public record UserInfoResponse(
        Long id,
        String username,
        String email,
        String role
) {
}
