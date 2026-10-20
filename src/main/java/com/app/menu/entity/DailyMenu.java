package com.app.menu.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle de domaine représentant les 3 repas du jour générés pour un utilisateur ou visiteur.
 * (Persistance gérée via Spring JdbcClient conformément au cahier des charges).
 */
public class DailyMenu {

    private Long id;
    private Long userProfileId;
    private LocalDate menuDate;
    private LocalDateTime generatedAt;
    private List<DailyMeal> meals = new ArrayList<>();

    public DailyMenu() {
        this.generatedAt = LocalDateTime.now();
    }

    public DailyMenu(Long userProfileId, LocalDate menuDate) {
        this.userProfileId = userProfileId;
        this.menuDate = menuDate;
        this.generatedAt = LocalDateTime.now();
    }

    public DailyMenu(Long id, Long userProfileId, LocalDate menuDate, LocalDateTime generatedAt, List<DailyMeal> meals) {
        this.id = id;
        this.userProfileId = userProfileId;
        this.menuDate = menuDate;
        this.generatedAt = generatedAt;
        this.meals = (meals != null) ? meals : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<DailyMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<DailyMeal> meals) {
        this.meals = meals;
    }
}
