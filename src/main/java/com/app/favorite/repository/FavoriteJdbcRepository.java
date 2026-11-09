package com.app.favorite.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.app.favorite.entity.FavoriteMeal;

@Repository
public class FavoriteJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public FavoriteJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FavoriteMeal> rowMapper = (rs, rowNum) -> {
        FavoriteMeal meal = new FavoriteMeal();
        meal.setId(rs.getLong("id"));
        meal.setRecipeId(rs.getLong("recipe_id"));
        meal.setTitle(rs.getString("title"));
        meal.setImage(rs.getString("image"));
        return meal;
    };

    public List<FavoriteMeal> findAll() {
        String sql = "SELECT id, recipe_id, title, image FROM favorite_meals";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void save(Long recipeId, String title, String image) {
        String sql = "MERGE INTO favorite_meals (recipe_id, title, image) KEY(recipe_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, recipeId, title, image);
    }

    public void deleteByRecipeId(Long recipeId) {
        String sql = "DELETE FROM favorite_meals WHERE recipe_id = ?";
        jdbcTemplate.update(sql, recipeId);
    }
}