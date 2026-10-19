package com.app.menu.service;

import com.app.clientapi.RecipeGateway;
import com.app.menu.entity.DailyMeal;
import com.app.menu.entity.DailyMenu;
import com.app.menu.model.MealDetailDto;
import com.app.menu.model.MealDto;
import com.app.menu.model.MealsResponseDto;
import com.app.menu.repository.DailyMenuRepository;
import com.app.profile.entity.DietaryPreferences;
import com.app.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service métier du composant Menu.
 * Gère la persistance des repas quotidiens et l'application des préférences utilisateur.
 */
@Service
@Transactional
public class MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final RecipeGateway recipeGateway;
    private final ProfileService profileService;
    private final DailyMenuRepository dailyMenuRepository;

    public MenuService(RecipeGateway recipeGateway,
                       ProfileService profileService,
                       DailyMenuRepository dailyMenuRepository) {
        this.recipeGateway = recipeGateway;
        this.profileService = profileService;
        this.dailyMenuRepository = dailyMenuRepository;
    }

    /**
     * Récupère le menu du jour pour l'utilisateur (ou visiteur).
     * S'il a déjà été généré aujourd'hui, il est chargé depuis la base de données sans appel externe.
     * S'il n'existe pas encore, il est généré et sauvegardé.
     */
    public MealsResponseDto getTodayMenu(Long userProfileId) {
        LocalDate today = LocalDate.now();

        Optional<DailyMenu> existingMenu = (userProfileId != null)
                ? dailyMenuRepository.findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(today, userProfileId)
                : dailyMenuRepository.findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc(today);

        if (existingMenu.isPresent()) {
            log.info("Menu du jour ({}) déjà existant en BDD pour userId={} (0 appel Spoonacular)", today, userProfileId);
            List<MealDto> cachedMeals = existingMenu.get().getMeals().stream()
                    .map(this::toMealDto)
                    .toList();
            return new MealsResponseDto(cachedMeals);
        }

        log.info("Aucun menu du jour trouvé pour userId={}, première génération...", userProfileId);
        return regenerateTodayMenu(userProfileId);
    }

    /**
     * Régénère un nouveau menu du jour en tenant compte des préférences alimentaires du profil utilisateur.
     */
    public MealsResponseDto regenerateTodayMenu(Long userProfileId) {
        List<String> tags = Collections.emptyList();

        if (userProfileId != null) {
            DietaryPreferences prefs = profileService.getPreferences(userProfileId);
            tags = prefs.toSpoonacularTags();
            profileService.incrementRequestCount(userProfileId);
            log.info("Régénération du menu pour userId={} avec les préférences : {}", userProfileId, tags);
        } else {
            log.info("Régénération d'un menu standard pour visiteur anonyme");
        }

        // Appel externe via RecipeGateway
        List<MealDto> generatedMeals = recipeGateway.getRandomRecipes(3, tags);

        // Sauvegarde en base de données pour persistance au refresh
        DailyMenu dailyMenu = new DailyMenu(userProfileId, LocalDate.now());
        List<DailyMeal> entityMeals = generatedMeals.stream()
                .map(this::toDailyMeal)
                .toList();
        dailyMenu.setMeals(entityMeals);
        dailyMenuRepository.save(dailyMenu);

        log.info("Nouveau menu du jour enregistré en base de données avec succès");
        return new MealsResponseDto(generatedMeals);
    }

    /**
     * Méthode générique aléatoire (MVP v0.1).
     */
    public List<MealDto> getRandomMenuSuggestions(int count) {
        int targetCount = Math.max(1, count);
        log.info("Récupération de {} suggestions de repas pour le menu", targetCount);
        return recipeGateway.getRandomRecipes(targetCount);
    }

    /**
     * Récupère les détails complets d'un plat par son identifiant.
     */
    @Transactional(readOnly = true)
    public MealDetailDto getMealDetails(Long id) {
        log.info("Récupération des détails pour le plat id={}", id);
        return recipeGateway.getRecipeDetails(id);
    }

    private MealDto toMealDto(DailyMeal entity) {
        return new MealDto(
                entity.getExternalRecipeId(),
                entity.getTitle(),
                entity.getImageUrl(),
                entity.getReadyInMinutes(),
                entity.getIsVegetarian(),
                entity.getIsVegan()
        );
    }

    private DailyMeal toDailyMeal(MealDto dto) {
        return new DailyMeal(
                dto.id(),
                dto.title(),
                dto.imageUrl(),
                dto.readyInMinutes(),
                dto.isVegetarian(),
                dto.isVegan()
        );
    }
}
