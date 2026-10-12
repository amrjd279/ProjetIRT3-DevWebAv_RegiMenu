package com.app.clientapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO brut représentant une recette telle que renvoyée par l'API Spoonacular.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpoonacularRecipeDto(
        Long id,
        String title,
        @JsonProperty("image")
        String imageUrl,
        Integer readyInMinutes,
        Integer servings,
        Boolean vegetarian,
        Boolean vegan,
        String summary,
        List<String> dishTypes
) {}
