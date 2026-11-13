package com.app.favorite.repository;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.app.favorite.entity.FavoriteMeal;

/**
 * Repository pour les favoris utilisant Spring JdbcClient (sans JPA).
 * Compatible H2 (local) et PostgreSQL (Render).
 */
@Repository
public class FavoriteJdbcRepository {

    private final JdbcClient jdbcClient;

    public FavoriteJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<FavoriteMeal> findAll(Long userProfileId) {
        String sql = """
                SELECT id, recipe_id, title, image
                FROM favorite_meals
                WHERE user_profile_id = :userProfileId
                ORDER BY id DESC
                """;
        return jdbcClient.sql(sql)
                .param("userProfileId", userProfileId)
                .query((rs, rowNum) -> {
                    FavoriteMeal meal = new FavoriteMeal();
                    meal.setId(rs.getLong("id"));
                    meal.setRecipeId(rs.getLong("recipe_id"));
                    meal.setTitle(rs.getString("title"));
                    meal.setImage(rs.getString("image"));
                    return meal;
                })
                .list();
    }

    public void save(Long userProfileId, Long recipeId, String title, String image) {
        Integer count = jdbcClient.sql("""
                SELECT COUNT(*)
                FROM favorite_meals
                WHERE user_profile_id = :userProfileId
                  AND recipe_id = :recipeId
                """)
                .param("userProfileId", userProfileId)
                .param("recipeId", recipeId)
                .query(Integer.class)
                .single();

        if (count != null && count > 0) {
            String updateSql = """
                    UPDATE favorite_meals
                    SET title = :title, image = :image
                    WHERE user_profile_id = :userProfileId
                      AND recipe_id = :recipeId
                    """;
            jdbcClient.sql(updateSql)
                    .param("userProfileId", userProfileId)
                    .param("title", title)
                    .param("image", image)
                    .param("recipeId", recipeId)
                    .update();
        } else {
            String insertSql = """
                    INSERT INTO favorite_meals
                        (user_profile_id, recipe_id, title, image)
                    VALUES
                        (:userProfileId, :recipeId, :title, :image)
                    """;
            jdbcClient.sql(insertSql)
                    .param("userProfileId", userProfileId)
                    .param("recipeId", recipeId)
                    .param("title", title)
                    .param("image", image)
                    .update();
        }
    }

    public void deleteByRecipeId(Long userProfileId, Long recipeId) {
        String sql = """
                DELETE FROM favorite_meals
                WHERE user_profile_id = :userProfileId
                  AND recipe_id = :recipeId
                """;
        jdbcClient.sql(sql)
                .param("userProfileId", userProfileId)
                .param("recipeId", recipeId)
                .update();
    }
}
