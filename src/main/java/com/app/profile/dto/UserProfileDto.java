package com.app.profile.dto;

/**
 * Données renvoyées au frontend React.
 */
public record UserProfileDto(
        Long id,
        String username,
        String email,
        boolean isAllergyPeanut,
        boolean isAllergyDairy,
        boolean isAllergyGluten,
        boolean isAllergyEgg,
        boolean isAllergySeafood,
        boolean isVegetarian,
        boolean isVegan,
        boolean isGlutenFree,
        boolean isLactoseFree,
        int requestCount
) {
}