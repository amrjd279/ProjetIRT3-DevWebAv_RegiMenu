package com.app.menu.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant les 3 repas du jour générés pour un utilisateur ou visiteur.
 */
@Entity
@Table(name = "daily_menus")
public class DailyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identifiant de l'utilisateur (ou null si visiteur anonyme)
    private Long userProfileId;

    private LocalDate menuDate;

    private LocalDateTime generatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "daily_menu_id")
    private List<DailyMeal> meals = new ArrayList<>();

    public DailyMenu() {}

    public DailyMenu(Long userProfileId, LocalDate menuDate) {
        this.userProfileId = userProfileId;
        this.menuDate = menuDate;
        this.generatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
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

