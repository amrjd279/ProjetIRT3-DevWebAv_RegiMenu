package com.app.profile.dto;

public record CreateProfileDto(
        String username,
        String email,
        boolean isVegetarian,
        boolean isVegan,
        boolean isGlutenFree,
        boolean isLactoseFree
) {
}