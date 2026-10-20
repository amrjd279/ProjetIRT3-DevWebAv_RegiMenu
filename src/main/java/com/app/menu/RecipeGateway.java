package com.app.menu;

import com.app.menu.model.MealDetailDto;
import com.app.menu.model.MealDto;
import java.util.Collections;
import java.util.List;

/**
 * Port d'accès aux recettes externes (défini par le module Menu).
 * Le module clientapi implémente ce contrat (Architecture Hexagonale).
 */
public interface RecipeGateway {

    /**
     * Récupère une liste de recettes aléatoires sans filtre particulier.
     *
     * @param count Nombre de recettes demandées
     * @return Liste de recettes sous forme de DTOs internes
     */
    default List<MealDto> getRandomRecipes(int count) {
        return getRandomRecipes(count, Collections.emptyList());
    }

    /**
     * Récupère une liste de recettes aléatoires filtrées par des tags alimentaires.
     *
     * @param count Nombre de recettes demandées
     * @param tags  Tags de régime ou préférences
     * @return Liste de recettes sous forme de DTOs internes
     */
    List<MealDto> getRandomRecipes(int count, List<String> tags);

    /**
     * Récupère les détails complets d'une recette par son identifiant.
     *
     * @param id Identifiant unique de la recette
     * @return Détail de la recette sous forme de MealDetailDto
     */
    MealDetailDto getRecipeDetails(Long id);
}
