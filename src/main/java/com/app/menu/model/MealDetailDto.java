package com.app.menu.model;

import java.util.List;

/**
 * DTO interne représentant le détail complet d'un plat pour la page détail.
 * Correspond aux champs attendus par le composant React RecipeDetail.
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
        Boolean isLactoseFree,
        String summary,
        String instructions,
        String sourceUrl,
        List<IngredientDto> extendedIngredients,
        List<String> dishTypes
) {}
