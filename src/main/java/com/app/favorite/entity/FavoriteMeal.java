package com.app.favorite.entity;

public class FavoriteMeal {
    private Long id;
    private Long recipeId;
    private String title;
    private String image;

    public FavoriteMeal() {}

    public FavoriteMeal(Long recipeId, String title, String image) {
        this.recipeId = recipeId;
        this.title = title;
        this.image = image;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}