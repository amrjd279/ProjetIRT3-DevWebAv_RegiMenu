package com.app.profile.dto;

public record UserProfileDto(
        Long id,
        String username,
        String email,
        boolean isVegetarian,
        boolean isVegan,
        boolean isGlutenFree,
        boolean isLactoseFree,
        int requestCount
) {}

