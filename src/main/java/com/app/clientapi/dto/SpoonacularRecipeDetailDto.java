package com.app.clientapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO brut représentant les informations détaillées d'une recette Spoonacular (/recipes/{id}/information).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpoonacularRecipeDetailDto(
        Long id,
        String title,
        @JsonProperty("image")
        String imageUrl,
        Integer readyInMinutes,
        Integer servings,
        Boolean vegetarian,
        Boolean vegan,
        Boolean glutenFree,
        String summary,
        String instructions,
        String sourceUrl,
        List<SpoonacularIngredientDto> extendedIngredients,
        List<String> dishTypes
) {}

