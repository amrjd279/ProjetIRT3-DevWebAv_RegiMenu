package com.app.menu.controller;

import java.util.List;

import com.app.menu.model.MealDetailDto;
import com.app.menu.model.MealDto;
import com.app.menu.model.MealsResponseDto;
import com.app.menu.service.MenuService;
import com.app.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST exposant les endpoints du module Menu.
 */
@RestController
@RequestMapping("/api/meals")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Endpoint GET /api/meals/today (v0.2)
     * Récupère le menu du jour persistant au refresh.
     * Si déjà généré en base aujourd'hui, aucun appel Spoonacular n'est effectué.
     */
    @GetMapping("/today")
    public ResponseEntity<MealsResponseDto> getTodayMeals(
            @AuthenticationPrincipal SecurityUser currentUser) {
        Long userId = (currentUser != null) ? currentUser.getId() : null;
        log.info("Requête GET /api/meals/today reçue pour userId={}", userId);
        MealsResponseDto response = menuService.getTodayMenu(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint POST /api/meals/regenerate (v0.2)
     * Bouton explicite pour recalculer 3 nouveaux repas selon les préférences du profil utilisateur.
     */
    @PostMapping("/regenerate")
    public ResponseEntity<MealsResponseDto> regenerateMeals(
            @AuthenticationPrincipal SecurityUser currentUser) {
        Long userId = (currentUser != null) ? currentUser.getId() : null;
        log.info("Requête POST /api/meals/regenerate reçue pour userId={}", userId);
        MealsResponseDto response = menuService.regenerateTodayMenu(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint GET /api/meals/random (Compatibilité v0.1)
     * Récupère une liste de repas aléatoires.
     */
    @GetMapping("/random")
    public ResponseEntity<MealsResponseDto> getRandomMeals(
            @RequestParam(name = "count", defaultValue = "3") int count) {
        log.info("Requête GET /api/meals/random reçue pour count={}", count);

        List<MealDto> meals = menuService.getRandomMenuSuggestions(count);
        return ResponseEntity.ok(new MealsResponseDto(meals));
    }

    /**
     * Endpoint GET /api/meals/{id} (et alias /api/meals/{id}/information)
     * Récupère les détails complets d'une recette pour la page détail (ingrédients, instructions, etc.).
     */
    @GetMapping({ "/{id}", "/{id}/information" })
    public ResponseEntity<MealDetailDto> getMealDetails(@PathVariable("id") Long id) {
        log.info("Requête GET /api/meals/{} reçue pour les détails", id);

        MealDetailDto detail = menuService.getMealDetails(id);
        return ResponseEntity.ok(detail);
    }
}