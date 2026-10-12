package com.app.menu.model;

/**
 * DTO interne représentant un plat dans le domaine Menu,
 * exposé aux consommateurs (ex: frontend React).
 */
public record MealDto(
        Long id,
        String title,
        String imageUrl,
        Integer readyInMinutes,
        Boolean isVegetarian,
        Boolean isVegan
) {}
