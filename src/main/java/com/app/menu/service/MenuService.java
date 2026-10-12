package com.app.menu.service;

import com.app.clientapi.RecipeGateway;
import com.app.menu.model.MealDetailDto;
import com.app.menu.model.MealDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier du composant Menu.
 * Injecte l'interface RecipeGateway (inversion de dépendance).
 */
@Service
public class MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final RecipeGateway recipeGateway;

    public MenuService(RecipeGateway recipeGateway) {
        this.recipeGateway = recipeGateway;
    }

    /**
     * Récupère des suggestions de repas aléatoires.
     *
     * @param count Nombre de repas souhaité
     * @return Liste de MealDto
     */
    public List<MealDto> getRandomMenuSuggestions(int count) {
        int targetCount = Math.max(1, count);
        log.info("Récupération de {} suggestions de repas pour le menu", targetCount);
        return recipeGateway.getRandomRecipes(targetCount);
    }

    /**
     * Récupère les détails complets d'un plat par son identifiant.
     *
     * @param id Identifiant unique du plat
     * @return Détails complets sous forme de MealDetailDto
     */
    public MealDetailDto getMealDetails(Long id) {
        log.info("Récupération des détails pour le plat id={}", id);
        return recipeGateway.getRecipeDetails(id);
    }
}
