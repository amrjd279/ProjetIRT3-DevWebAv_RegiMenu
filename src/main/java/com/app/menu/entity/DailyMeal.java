package com.app.menu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entité stockant un plat associé à un menu quotidien en base de données.
 */
@Entity
@Table(name = "daily_meals")
public class DailyMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long externalRecipeId;

    private String title;

    private String imageUrl;

    private Integer readyInMinutes;

    private Boolean isVegetarian;

    private Boolean isVegan;

    public DailyMeal() {}

    public DailyMeal(Long externalRecipeId, String title, String imageUrl, Integer readyInMinutes, Boolean isVegetarian, Boolean isVegan) {
        this.externalRecipeId = externalRecipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.readyInMinutes = readyInMinutes;
        this.isVegetarian = isVegetarian;
        this.isVegan = isVegan;
    }

    public Long getId() {
        return id;
    }

    public Long getExternalRecipeId() {
        return externalRecipeId;
    }

    public void setExternalRecipeId(Long externalRecipeId) {
        this.externalRecipeId = externalRecipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Boolean getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(Boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    public Boolean getIsVegan() {
        return isVegan;
    }

    public void setIsVegan(Boolean isVegan) {
        this.isVegan = isVegan;
    }
}

