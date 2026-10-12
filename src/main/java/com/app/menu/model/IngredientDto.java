package com.app.menu.model;

/**
 * DTO interne représentant un ingrédient d'une recette.
 */
public record IngredientDto(
        Long id,
        String name,
        String original,
        Double amount,
        String unit
) {}

