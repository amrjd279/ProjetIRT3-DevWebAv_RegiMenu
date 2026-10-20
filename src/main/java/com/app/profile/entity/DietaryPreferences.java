package com.app.profile.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Préférences alimentaires de l'utilisateur.
 * Cet objet est stocké dans l'objet UserProfile.
 */
public class DietaryPreferences {

    private boolean vegetarian = false;
    private boolean vegan = false;
    private boolean glutenFree = false;
    private boolean lactoseFree = false;

    public DietaryPreferences() {}

    public DietaryPreferences(boolean vegetarian, boolean vegan, boolean glutenFree, boolean lactoseFree) {
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.lactoseFree = lactoseFree;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isLactoseFree() {
        return lactoseFree;
    }

    public void setLactoseFree(boolean lactoseFree) {
        this.lactoseFree = lactoseFree;
    }

    /**
     * Convertit les préférences actives en tags compatibles avec l'API Spoonacular.
     */
    public List<String> toSpoonacularTags() {
        List<String> tags = new ArrayList<>();
        if (vegan) {
            tags.add("vegan");
        } else if (vegetarian) {
            tags.add("vegetarian");
        }
        if (glutenFree) {
            tags.add("gluten free");
        }
        if (lactoseFree) {
            tags.add("dairy free");
        }
        return tags;
    }
}
