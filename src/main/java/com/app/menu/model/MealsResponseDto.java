package com.app.menu.model;

import java.util.List;

/**
 * Enveloppe de réponse contenant la liste des plats ("meals": [...]).
 */
public record MealsResponseDto(
        List<MealDto> meals
) {}

