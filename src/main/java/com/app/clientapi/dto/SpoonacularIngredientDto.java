package com.app.clientapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO brut représentant un ingrédient tel que renvoyé par l'API Spoonacular.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpoonacularIngredientDto(
        Long id,
        String name,
        String original,
        Double amount,
        String unit
) {}

