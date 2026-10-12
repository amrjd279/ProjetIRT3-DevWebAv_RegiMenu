package com.app.clientapi.mapper;

import com.app.clientapi.dto.SpoonacularRecipeDto;
import com.app.clientapi.dto.SpoonacularResponseDto;
import com.app.menu.model.MealDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Mapper (Anti-Corruption Layer) transformant les DTOs bruts de Spoonacular en DTO métier MealDto.
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
                String.valueOf(dto.id()),
                dto.title(),
                dto.imageURL(),
                dto.readyInMinutes(),
                dto.servings()
        );
    }
}

