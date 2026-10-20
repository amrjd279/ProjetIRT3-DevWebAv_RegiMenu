package com.app.clientapi.mapper;

import com.app.clientapi.dto.SpoonacularRecipeDto;
import com.app.clientapi.dto.SpoonacularResponseDto;
import com.app.menu.model.MealDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeMapperTest {

    private RecipeMapper recipeMapper;

    @BeforeEach
    void setUp() {
        recipeMapper = new RecipeMapper();
    }

    /**
     * Test 1 (Exemple 1/3 - Fonctionnel) :
     * Vérifie la bonne transformation d'un SpoonacularRecipeDto brut vers le MealDto métier.
     */
    @Test
    @DisplayName("Devrait mapper correctement un SpoonacularRecipeDto en MealDto")
    void shouldMapSpoonacularDtoToMealDto() {
        // GIVEN : données brutes Spoonacular
        SpoonacularRecipeDto rawDto = new SpoonacularRecipeDto(
                633166L,
                "Soupe Tortilla",
                "https://img.spoonacular.com/recipes/633166-556x370.jpg",
                45,
                4,
                true,   // vegetarian
                false,  // vegan
                "Délicieuse soupe",
                List.of("soup")
        );

        // WHEN : conversion via le mapper
        MealDto mealDto = recipeMapper.toMealDto(rawDto);

        // THEN : vérification des assertions
        assertThat(mealDto).isNotNull();
        assertThat(mealDto.id()).isEqualTo(633166L);
        assertThat(mealDto.title()).isEqualTo("Soupe Tortilla");
        assertThat(mealDto.imageUrl()).isEqualTo("https://img.spoonacular.com/recipes/633166-556x370.jpg");
        assertThat(mealDto.readyInMinutes()).isEqualTo(45);
        assertThat(mealDto.isVegetarian()).isTrue();
        assertThat(mealDto.isVegan()).isFalse();
    }

    /**
     * Test 2 (Placeholder Étudiant 2/3 - À toi de jouer !) :
     * 
     * Objectif : Vérifier que `toMealDtoList` gère les cas limites sans crasher
     * (quand la réponse est null ou quand la liste de recettes est null).
     * 
     * Consigne :
     * 1. Appelle `recipeMapper.toMealDtoList(null)` et vérifie que le résultat est vide (`isEmpty()`).
     * 2. Crée un `SpoonacularResponseDto` avec `null` et vérifie aussi que la liste est vide.
     */
    @Test
    @DisplayName("Devrait renvoyer une liste vide si la réponse Spoonacular est nulle")
    void studentTask_shouldReturnEmptyListWhenResponseIsNull() {
        // TODO: ÉTUDIANT - Écris ton test ici :
        // List<MealDto> results = recipeMapper.toMealDtoList(null);
        // assertThat(results).isNotNull().isEmpty();
    }

    /**
     * Test 3 (Placeholder Étudiant 3/3 - À toi de jouer !) :
     * 
     * Objectif : Vérifier le mapping vers `MealDetailDto` pour la page de détail d'une recette.
     * 
     * Consigne :
     * 1. Instancie un `SpoonacularRecipeDetailDto` avec des ingrédients et un `dairyFree` à true.
     * 2. Appelle `recipeMapper.toMealDetailDto(...)`.
     * 3. Vérifie que `isLactoseFree` vaut bien true et que les ingrédients sont présents.
     */
    @Test
    @DisplayName("Devrait mapper un SpoonacularRecipeDetailDto en MealDetailDto avec extendedIngredients")
    void studentTask_shouldMapDetailDtoCorrectly() {
        // TODO: ÉTUDIANT - Écris ton test ici en t'inspirant du Test 1 !
    }
}

