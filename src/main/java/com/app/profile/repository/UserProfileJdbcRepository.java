package com.app.profile.repository;

import com.app.profile.entity.DietaryPreferences;
import com.app.profile.entity.UserProfile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation JdbcClient pour le profil utilisateur (conforme aux exigences de la section 4.1).
 */
@Repository
public class UserProfileJdbcRepository implements UserProfileRepository {

    private final JdbcClient jdbcClient;

    public UserProfileJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<UserProfile> findById(Long id) {
        String sql = """
            SELECT id, username, email,
                   allergy_peanut, allergy_dairy, allergy_gluten, allergy_egg, allergy_seafood,
                   is_vegetarian, is_vegan, is_gluten_free, is_lactose_free,
                   request_count, created_at
            FROM user_profiles
            WHERE id = :id
            """;
        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> mapRowToUserProfile(rs))
                .optional();
    }

    @Override
    public Optional<UserProfile> findByEmail(String email) {
        String sql = """
            SELECT id, username, email,
                   allergy_peanut, allergy_dairy, allergy_gluten, allergy_egg, allergy_seafood,
                   is_vegetarian, is_vegan, is_gluten_free, is_lactose_free,
                   request_count, created_at
            FROM user_profiles
            WHERE email = :email
            """;
        return jdbcClient.sql(sql)
                .param("email", email)
                .query((rs, rowNum) -> mapRowToUserProfile(rs))
                .optional();
    }

    @Override
    public Optional<UserProfile> findByUsername(String username) {
        String sql = """
            SELECT id, username, email,
                   allergy_peanut, allergy_dairy, allergy_gluten, allergy_egg, allergy_seafood,
                   is_vegetarian, is_vegan, is_gluten_free, is_lactose_free,
                   request_count, created_at
            FROM user_profiles
            WHERE username = :username
            """;
        return jdbcClient.sql(sql)
                .param("username", username)
                .query((rs, rowNum) -> mapRowToUserProfile(rs))
                .optional();
    }

    @Override
    public List<UserProfile> findAll() {
        String sql = """
            SELECT id, username, email,
                   allergy_peanut, allergy_dairy, allergy_gluten, allergy_egg, allergy_seafood,
                   is_vegetarian, is_vegan, is_gluten_free, is_lactose_free,
                   request_count, created_at
            FROM user_profiles
            ORDER BY id ASC
            """;
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> mapRowToUserProfile(rs))
                .list();
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        DietaryPreferences prefs = userProfile.getPreferences();
        if (userProfile.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = """
                INSERT INTO user_profiles (
                    username, email,
                    allergy_peanut, allergy_dairy, allergy_gluten, allergy_egg, allergy_seafood,
                    is_vegetarian, is_vegan, is_gluten_free, is_lactose_free,
                    request_count, created_at
                )
                VALUES (
                    :username, :email,
                    :allergyPeanut, :allergyDairy, :allergyGluten, :allergyEgg, :allergySeafood,
                    :isVegetarian, :isVegan, :isGlutenFree, :isLactoseFree,
                    :requestCount, :createdAt
                )
                """;

            jdbcClient.sql(sql)
                    .param("username", userProfile.getUsername())
                    .param("email", userProfile.getEmail())
                    .param("allergyPeanut", prefs != null && prefs.isAllergyPeanut())
                    .param("allergyDairy", prefs != null && prefs.isAllergyDairy())
                    .param("allergyGluten", prefs != null && prefs.isAllergyGluten())
                    .param("allergyEgg", prefs != null && prefs.isAllergyEgg())
                    .param("allergySeafood", prefs != null && prefs.isAllergySeafood())
                    .param("isVegetarian", prefs != null && prefs.isVegetarian())
                    .param("isVegan", prefs != null && prefs.isVegan())
                    .param("isGlutenFree", prefs != null && prefs.isGlutenFree())
                    .param("isLactoseFree", prefs != null && prefs.isLactoseFree())
                    .param("requestCount", userProfile.getRequestCount())
                    .param("createdAt", userProfile.getCreatedAt() != null ? userProfile.getCreatedAt() : LocalDateTime.now())
                    .update(keyHolder, "id");

            Number key = keyHolder.getKey();
            if (key != null) {
                userProfile.setId(key.longValue());
            }
        } else {
            String sql = """
                UPDATE user_profiles
                SET username = :username,
                    email = :email,
                    allergy_peanut = :allergyPeanut,
                    allergy_dairy = :allergyDairy,
                    allergy_gluten = :allergyGluten,
                    allergy_egg = :allergyEgg,
                    allergy_seafood = :allergySeafood,
                    is_vegetarian = :isVegetarian,
                    is_vegan = :isVegan,
                    is_gluten_free = :isGlutenFree,
                    is_lactose_free = :isLactoseFree,
                    request_count = :requestCount
                WHERE id = :id
                """;

            jdbcClient.sql(sql)
                    .param("username", userProfile.getUsername())
                    .param("email", userProfile.getEmail())
                    .param("allergyPeanut", prefs != null && prefs.isAllergyPeanut())
                    .param("allergyDairy", prefs != null && prefs.isAllergyDairy())
                    .param("allergyGluten", prefs != null && prefs.isAllergyGluten())
                    .param("allergyEgg", prefs != null && prefs.isAllergyEgg())
                    .param("allergySeafood", prefs != null && prefs.isAllergySeafood())
                    .param("isVegetarian", prefs != null && prefs.isVegetarian())
                    .param("isVegan", prefs != null && prefs.isVegan())
                    .param("isGlutenFree", prefs != null && prefs.isGlutenFree())
                    .param("isLactoseFree", prefs != null && prefs.isLactoseFree())
                    .param("requestCount", userProfile.getRequestCount())
                    .param("id", userProfile.getId())
                    .update();
        }
        return userProfile;
    }

    private UserProfile mapRowToUserProfile(java.sql.ResultSet rs) throws java.sql.SQLException {
        UserProfile profile = new UserProfile(rs.getString("username"), rs.getString("email"));
        profile.setId(rs.getLong("id"));
        DietaryPreferences prefs = new DietaryPreferences(
                rs.getBoolean("allergy_peanut"),
                rs.getBoolean("allergy_dairy"),
                rs.getBoolean("allergy_gluten"),
                rs.getBoolean("allergy_egg"),
                rs.getBoolean("allergy_seafood"),
                rs.getBoolean("is_vegetarian"),
                rs.getBoolean("is_vegan"),
                rs.getBoolean("is_gluten_free"),
                rs.getBoolean("is_lactose_free")
        );
        profile.setPreferences(prefs);
        profile.setRequestCount(rs.getInt("request_count"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            profile.setCreatedAt(ts.toLocalDateTime());
        }
        return profile;
    }
}
