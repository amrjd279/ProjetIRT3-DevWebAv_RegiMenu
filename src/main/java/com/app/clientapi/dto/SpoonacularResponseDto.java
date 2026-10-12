package com.app.clientapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * DTO brut représentant la réponse enveloppe de l'API Spoonacular pour /recipes/random.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpoonacularResponseDto(
        List<SpoonacularRecipeDto> recipes
) {}

