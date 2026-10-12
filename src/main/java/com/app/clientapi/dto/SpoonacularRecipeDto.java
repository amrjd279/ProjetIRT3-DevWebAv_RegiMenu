package com.app.clientapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * DTO(Data Transfer Object) brut représentant une recette telle que renvoyée par l'API Spoonacular.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpoonacularRecipeDto(
        Long id,
        String title,
        String imageURL,
        Integer readyInMinutes,
        Integer servings,
        String summary,
        List<String> dishTypes
) {}

