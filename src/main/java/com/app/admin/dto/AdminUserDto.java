package com.app.admin.dto;

import java.time.LocalDateTime;

/**
 * Vue d'administration d'un utilisateur, sans données sensibles.
 */
public record AdminUserDto(
        Long id,
        String username,
        String email,
        String role,
        String status,
        int requestCount,
        LocalDateTime createdAt
) {
}
