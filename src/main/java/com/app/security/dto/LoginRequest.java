package com.app.security.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Payload de connexion acceptant un nom d'utilisateur ou un email sous le champ
 * {@code identifier} (ou ses alias JSON {@code username}, {@code email}, {@code login}).
 */
public record LoginRequest(
        @JsonAlias({"username", "email", "login"}) String identifier,
        String password
) {
}
