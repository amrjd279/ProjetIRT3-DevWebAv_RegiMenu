package com.app.menu.model;

import java.util.List;

/**
 * DTO interne représentant le détail complet d'un plat pour la page détail.
 */
public record MealDetailDto(
        Long id,
        String title,
        String imageUrl,
        Integer readyInMinutes,
        Integer servings,
        Boolean isVegetarian,
        Boolean isVegan,
        Boolean isGlutenFree,
        String summary,
        String instructions,
        String sourceUrl,
        List<IngredientDto> ingredients,
        List<String> dishTypes
) {}

