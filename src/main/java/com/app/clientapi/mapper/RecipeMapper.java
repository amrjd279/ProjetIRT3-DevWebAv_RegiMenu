package com.app.clientapi.mapper;

import com.app.clientapi.dto.SpoonacularIngredientDto;
import com.app.clientapi.dto.SpoonacularRecipeDetailDto;
import com.app.clientapi.dto.SpoonacularRecipeDto;
import com.app.clientapi.dto.SpoonacularResponseDto;
import com.app.menu.model.IngredientDto;
import com.app.menu.model.MealDetailDto;
import com.app.menu.model.MealDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Mapper (Anti-Corruption Layer) transformant les DTOs bruts de Spoonacular en DTOs métier.
 */
@Component
public class RecipeMapper {

    public List<MealDto> toMealDtoList(SpoonacularResponseDto response) {
        if (response == null || response.recipes() == null) {
            return Collections.emptyList();
        }
        return response.recipes().stream()
                .map(this::toMealDto)
                .toList();
    }

    public MealDto toMealDto(SpoonacularRecipeDto dto) {
        if (dto == null) {
            return null;
        }
        return new MealDto(
                dto.id(),
                dto.title(),
                dto.imageUrl(),
                dto.readyInMinutes(),
                dto.vegetarian() != null ? dto.vegetarian() : false,
                dto.vegan() != null ? dto.vegan() : false
        );
    }

    public MealDetailDto toMealDetailDto(SpoonacularRecipeDetailDto dto) {
        if (dto == null) {
            return null;
        }
        List<IngredientDto> ingredients = dto.extendedIngredients() != null
                ? dto.extendedIngredients().stream()
                        .map(this::toIngredientDto)
                        .toList()
                : Collections.emptyList();

        return new MealDetailDto(
                dto.id(),
                dto.title(),
                dto.imageUrl(),
                dto.readyInMinutes(),
                dto.servings(),
                dto.vegetarian() != null ? dto.vegetarian() : false,
                dto.vegan() != null ? dto.vegan() : false,
                dto.glutenFree() != null ? dto.glutenFree() : false,
                dto.dairyFree() != null ? dto.dairyFree() : false,
                dto.summary(),
                dto.instructions(),
                dto.sourceUrl(),
                ingredients,
                dto.dishTypes() != null ? dto.dishTypes() : Collections.emptyList()
        );
    }

    public IngredientDto toIngredientDto(SpoonacularIngredientDto dto) {
        if (dto == null) {
            return null;
        }
        return new IngredientDto(
                dto.id(),
                dto.name(),
                dto.original(),
                dto.amount(),
                dto.unit()
        );
    }
}
