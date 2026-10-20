package com.app.menu.repository;

import com.app.menu.entity.DailyMeal;
import com.app.menu.entity.DailyMenu;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository Menu utilisant Spring JdbcClient (exigé par le cahier des charges).
 * Toutes les requêtes SQL sont écrites explicitement et paramétrées.
 */
@Repository
public class DailyMenuJdbcRepository implements DailyMenuRepository {

    private final JdbcClient jdbcClient;

    public DailyMenuJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<DailyMenu> findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(LocalDate menuDate, Long userProfileId) {
        String sql = """
            SELECT id, user_profile_id, menu_date, generated_at
            FROM daily_menus
            WHERE menu_date = :menuDate AND user_profile_id = :userProfileId
            ORDER BY generated_at DESC
            LIMIT 1
            """;

        Optional<DailyMenu> menuOpt = jdbcClient.sql(sql)
                .param("menuDate", menuDate)
                .param("userProfileId", userProfileId)
                .query((rs, rowNum) -> new DailyMenu(
                        rs.getLong("id"),
                        rs.getObject("user_profile_id", Long.class),
                        rs.getDate("menu_date").toLocalDate(),
                        rs.getTimestamp("generated_at").toLocalDateTime(),
                        null
                ))
                .optional();

        menuOpt.ifPresent(this::loadMealsForMenu);
        return menuOpt;
    }

    @Override
    public Optional<DailyMenu> findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc(LocalDate menuDate) {
        String sql = """
            SELECT id, user_profile_id, menu_date, generated_at
            FROM daily_menus
            WHERE menu_date = :menuDate AND user_profile_id IS NULL
            ORDER BY generated_at DESC
            LIMIT 1
            """;

        Optional<DailyMenu> menuOpt = jdbcClient.sql(sql)
                .param("menuDate", menuDate)
                .query((rs, rowNum) -> new DailyMenu(
                        rs.getLong("id"),
                        null,
                        rs.getDate("menu_date").toLocalDate(),
                        rs.getTimestamp("generated_at").toLocalDateTime(),
                        null
                ))
                .optional();

        menuOpt.ifPresent(this::loadMealsForMenu);
        return menuOpt;
    }

    @Override
    @Transactional
    public DailyMenu save(DailyMenu dailyMenu) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertMenuSql = """
            INSERT INTO daily_menus (user_profile_id, menu_date, generated_at)
            VALUES (:userProfileId, :menuDate, :generatedAt)
            """;

        LocalDateTime generatedAt = dailyMenu.getGeneratedAt() != null ? dailyMenu.getGeneratedAt() : LocalDateTime.now();
        dailyMenu.setGeneratedAt(generatedAt);

        jdbcClient.sql(insertMenuSql)
                .param("userProfileId", dailyMenu.getUserProfileId())
                .param("menuDate", dailyMenu.getMenuDate())
                .param("generatedAt", generatedAt)
                .update(keyHolder, "id");

        Number key = keyHolder.getKey();
        if (key != null) {
            dailyMenu.setId(key.longValue());
        }

        if (dailyMenu.getMeals() != null && !dailyMenu.getMeals().isEmpty()) {
            String insertMealSql = """
                INSERT INTO daily_meals (daily_menu_id, external_recipe_id, title, image_url, ready_in_minutes, is_vegetarian, is_vegan)
                VALUES (:dailyMenuId, :externalRecipeId, :title, :imageUrl, :readyInMinutes, :isVegetarian, :isVegan)
                """;

            for (DailyMeal meal : dailyMenu.getMeals()) {
                KeyHolder mealKeyHolder = new GeneratedKeyHolder();
                meal.setDailyMenuId(dailyMenu.getId());

                jdbcClient.sql(insertMealSql)
                        .param("dailyMenuId", dailyMenu.getId())
                        .param("externalRecipeId", meal.getExternalRecipeId())
                        .param("title", meal.getTitle())
                        .param("imageUrl", meal.getImageUrl())
                        .param("readyInMinutes", meal.getReadyInMinutes())
                        .param("isVegetarian", meal.getIsVegetarian() != null ? meal.getIsVegetarian() : false)
                        .param("isVegan", meal.getIsVegan() != null ? meal.getIsVegan() : false)
                        .update(mealKeyHolder, "id");

                Number mealKey = mealKeyHolder.getKey();
                if (mealKey != null) {
                    meal.setId(mealKey.longValue());
                }
            }
        }

        return dailyMenu;
    }

    private void loadMealsForMenu(DailyMenu menu) {
        String sql = """
            SELECT id, daily_menu_id, external_recipe_id, title, image_url, ready_in_minutes, is_vegetarian, is_vegan
            FROM daily_meals
            WHERE daily_menu_id = :dailyMenuId
            ORDER BY id ASC
            """;

        List<DailyMeal> meals = jdbcClient.sql(sql)
                .param("dailyMenuId", menu.getId())
                .query((rs, rowNum) -> new DailyMeal(
                        rs.getLong("id"),
                        rs.getLong("daily_menu_id"),
                        rs.getLong("external_recipe_id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        rs.getInt("ready_in_minutes"),
                        rs.getBoolean("is_vegetarian"),
                        rs.getBoolean("is_vegan")
                ))
                .list();

        menu.setMeals(meals);
    }
}
