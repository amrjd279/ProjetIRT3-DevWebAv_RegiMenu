package com.app.profile.dto;

/**
 * Données reçues par : PUT /api/profile
 */
public record UpdatePreferencesDto(
        boolean isAllergyPeanut,
        boolean isAllergyDairy,
        boolean isAllergyGluten,
        boolean isAllergyEgg,
        boolean isAllergySeafood,
        boolean isVegetarian,
        boolean isVegan,
        boolean isGlutenFree,
        boolean isLactoseFree
) {
}