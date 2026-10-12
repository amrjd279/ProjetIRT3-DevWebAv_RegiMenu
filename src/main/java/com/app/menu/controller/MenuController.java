package com.app.menu.controller;

import com.app.menu.model.MealDto;
import com.app.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
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
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) { // Injection de dépendance du service MenuService
        this.menuService = menuService;
    }

    /**
     * Endpoint GET /api/meals/random
     *
     * @param count Nombre de repas à renvoyer (défaut : 3)
     * @return Liste de MealDto
     */
    @GetMapping("/random")
    public ResponseEntity<List<MealDto>> getRandomMeals(
            @RequestParam(name = "count", defaultValue = "3") int count) {
        /*List<MealDto> meals = menuService.getRandomMenuSuggestions(count);
        return ResponseEntity.ok(meals);*/
        return List.of(
        new MealDto(1L, "Avocado, Corn, Tomato and Tortilla Soup", "url_image_1", 45, true, false),
        new MealDto(2L, "Peach Crostata", "url_image_2", 45, true, false),
        new MealDto(3L, "Grilled Chicken Salad", "url_image_3", 30, false, false)
        );

    }
}

