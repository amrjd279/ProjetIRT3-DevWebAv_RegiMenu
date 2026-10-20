package com.app.profile.dto;

/**
 * Données renvoyées au frontend React.
 *
 * On ne renvoie pas directement l'entité JPA.
 *
 * On utilise un DTO pour contrôler les données envoyées.
 */
public record UserProfileDto(

        Long id,

        String username,

        String email,

        boolean isVegetarian,

        boolean isVegan,

        boolean isGlutenFree,

        boolean isLactoseFree,

        int requestCount

) {
}