package com.app.security.repository;

import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.security.SecurityUser;

/**
 * Accès aux données d'authentification dans la table user_profiles via Spring JdbcClient (sans JPA).
 * Permet de garder le module Security autonome vis-à-vis des autres modules métier.
 */
@Repository
public class AuthUserJdbcRepository {

    private final JdbcClient jdbcClient;

    public AuthUserJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<SecurityUser> findByUsernameOrEmail(String identifier) {
        String sql = """
            SELECT id, username, email, password_hash, role, status
            FROM user_profiles
            WHERE username = :identifier OR email = :identifier
            """;

        return jdbcClient.sql(sql)
                .param("identifier", identifier)
                .query((rs, rowNum) -> new SecurityUser(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("status")
                ))
                .optional();
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM user_profiles WHERE username = :username")
                .param("username", username)
                .query(Integer.class)
                .single();
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM user_profiles WHERE email = :email")
                .param("email", email)
                .query(Integer.class)
                .single();
        return count != null && count > 0;
    }

    public SecurityUser createUser(String username, String email, String passwordHash, String role) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = """
            INSERT INTO user_profiles (username, email, password_hash, role)
            VALUES (:username, :email, :passwordHash, :role)
            """;

        jdbcClient.sql(sql)
                .param("username", username)
                .param("email", email)
                .param("passwordHash", passwordHash)
                .param("role", role)
                .update(keyHolder, "id");

        Number key = keyHolder.getKey();
        Long id = (key != null) ? key.longValue() : null;
        return new SecurityUser(id, username, email, passwordHash, role);
    }
}
