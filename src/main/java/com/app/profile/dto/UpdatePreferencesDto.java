package com.app.profile.dto;

public record UpdatePreferencesDto(
        boolean isVegetarian,
        boolean isVegan,
        boolean isGlutenFree,
        boolean isLactoseFree
) {}

