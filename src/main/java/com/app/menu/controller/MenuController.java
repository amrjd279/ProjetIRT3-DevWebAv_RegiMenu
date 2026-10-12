package com.app.menu.controller;

import com.app.menu.model.MealDto;
import com.app.menu.model.MealsResponseDto;
import com.app.menu.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * Endpoint GET /api/meals/random
     * Appelle le service métier MenuService qui délègue à RecipeGateway (Spoonacular)
     * et convertit les données via RecipeMapper vers MealDto.
     *
     * @param count Nombre de repas aléatoires à renvoyer (défaut : 3)
     * @return Objet MealsResponseDto contenant la liste des repas sous la clé "meals"
     */
    @GetMapping("/random")
    public ResponseEntity<MealsResponseDto> getRandomMeals(
            @RequestParam(name = "count", defaultValue = "3") int count) {
        log.info("Requête GET /api/meals/random reçue pour count={}", count);

        List<MealDto> meals = menuService.getRandomMenuSuggestions(count);
        return ResponseEntity.ok(new MealsResponseDto(meals));
    }
}
