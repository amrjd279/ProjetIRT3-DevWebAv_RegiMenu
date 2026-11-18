package com.app.menu.service;

import com.app.menu.RecipeGateway;
import com.app.menu.entity.DailyMeal;
import com.app.menu.entity.DailyMenu;
import com.app.menu.exception.MenuAlreadyValidatedException;
import com.app.menu.model.MealDto;
import com.app.menu.model.MealsResponseDto;
import com.app.menu.repository.DailyMenuRepository;
import com.app.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private RecipeGateway recipeGateway;

    @Mock
    private ProfileService profileService;

    @Mock
    private DailyMenuRepository dailyMenuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void shouldValidateTodayMenuSuccessfully() {
        Long userId = 42L;
        LocalDate today = LocalDate.now();

        DailyMeal meal = new DailyMeal(1L, 10L, 100L, "Salade César", "http://image.jpg", 15, true, false);
        DailyMenu existingMenu = new DailyMenu(10L, userId, today, LocalDateTime.now().minusHours(1), false, null, List.of(meal));

        when(dailyMenuRepository.findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(today, userId))
                .thenReturn(Optional.of(existingMenu));

        MealsResponseDto response = menuService.validateTodayMenu(userId);

        assertThat(response).isNotNull();
        assertThat(response.validated()).isTrue();
        assertThat(response.validatedAt()).isNotNull();
        assertThat(response.meals()).hasSize(1);
        assertThat(response.meals().get(0).title()).isEqualTo("Salade César");

        verify(dailyMenuRepository).updateValidationStatus(eq(10L), eq(true), any(LocalDateTime.class));
    }

    @Test
    void shouldBlockRegenerationWhenMenuIsAlreadyValidated() {
        Long userId = 42L;
        LocalDate today = LocalDate.now();

        DailyMenu validatedMenu = new DailyMenu(10L, userId, today, LocalDateTime.now(), true, LocalDateTime.now(), List.of());

        when(dailyMenuRepository.findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(today, userId))
                .thenReturn(Optional.of(validatedMenu));

        assertThatThrownBy(() -> menuService.regenerateTodayMenu(userId))
                .isInstanceOf(MenuAlreadyValidatedException.class)
                .hasMessageContaining("déjà validé");

        verifyNoInteractions(recipeGateway);
    }

    @Test
    void shouldRejectValidationForAnonymousUser() {
        assertThatThrownBy(() -> menuService.validateTodayMenu(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("L'utilisateur doit être connecté");

        verifyNoInteractions(dailyMenuRepository);
    }
}
