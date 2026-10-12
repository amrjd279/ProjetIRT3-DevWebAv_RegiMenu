package com.app.menu.model;

/**
 * DTO interne épuré représentant un plat dans le domaine Menu.
 */
public record MealDto(
        String id,
        String name,
        String imageUrl,
        Integer preparationTimeInMinutes,
        Integer servings
) {}

