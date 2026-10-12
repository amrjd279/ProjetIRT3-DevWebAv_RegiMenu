package com.app.menu.controller;

import com.app.menu.model.MealDto;
import com.app.menu.model.MealsResponseDto;
import com.app.menu.service.MenuService;
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

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Endpoint GET /api/meals/random
     *
     * @param count Nombre de repas à renvoyer (défaut : 3)
     * @return Objet MealsResponseDto contenant la liste des repas sous la clé "meals"
     */
    @GetMapping("/random")
    public ResponseEntity<MealsResponseDto> getRandomMeals(
            @RequestParam(name = "count", defaultValue = "3") int count) {

        /*
         * Pour basculer sur la véritable API Spoonacular, décommentez ces 2 lignes :
         * List<MealDto> realMeals = menuService.getRandomMenuSuggestions(count);
         * return ResponseEntity.ok(new MealsResponseDto(realMeals));
         */

        // Mocks pour tests d'intégration avec le frontend React :
        List<MealDto> mockMeals = List.of(
                new MealDto(
                        633166L,
                        "Avocado, Corn, Tomato and Tortilla Soup",
                        "https://img.spoonacular.com/recipes/633166-556x370.jpg",
                        45,
                        false,
                        false
                ),
                new MealDto(
                        655219L,
                        "Peach Crostata",
                        "https://img.spoonacular.com/recipes/655219-556x370.jpg",
                        45,
                        true,
                        false
                ),
                new MealDto(
                        645479L,
                        "Grilled Chicken Salad",
                        "https://img.spoonacular.com/recipes/645479-556x370.jpg",
                        30,
                        false,
                        false
                )
        );

        return ResponseEntity.ok(new MealsResponseDto(mockMeals));
    }
}
