package com.app.profile.dto;

/**
 * Données reçues par :
 *
 * PUT /api/profile
 *
 * Ce DTO représente les choix envoyés par React.
 */
public record UpdatePreferencesDto(

        boolean isVegetarian,

        boolean isVegan,

        boolean isGlutenFree,

        boolean isLactoseFree

) {
}