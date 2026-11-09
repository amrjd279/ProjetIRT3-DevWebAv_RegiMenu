package com.app.profile.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Préférences alimentaires et allergies d'un utilisateur.
 * Modèle de domaine sans JPA (persistance assurée via Spring JdbcClient).
 */
public class DietaryPreferences {

    private Long id;

    // --- ALLERGIES (Sécurité / Santé) ---
    private boolean allergyPeanut = false;
    private boolean allergyDairy = false;
    private boolean allergyGluten = false;
    private boolean allergyEgg = false;
    private boolean allergySeafood = false;

    // --- PRÉFÉRENCES (Style de vie) ---
    private boolean vegetarian = false;
    private boolean vegan = false;
    private boolean glutenFree = false;
    private boolean lactoseFree = false;

    private UserProfile profile;

    public DietaryPreferences() {
    }

    public DietaryPreferences(
            boolean allergyPeanut,
            boolean allergyDairy,
            boolean allergyGluten,
            boolean allergyEgg,
            boolean allergySeafood,
            boolean vegetarian,
            boolean vegan,
            boolean glutenFree,
            boolean lactoseFree
    ) {
        this.allergyPeanut = allergyPeanut;
        this.allergyDairy = allergyDairy;
        this.allergyGluten = allergyGluten;
        this.allergyEgg = allergyEgg;
        this.allergySeafood = allergySeafood;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.lactoseFree = lactoseFree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAllergyPeanut() { return allergyPeanut; }
    public void setAllergyPeanut(boolean allergyPeanut) { this.allergyPeanut = allergyPeanut; }

    public boolean isAllergyDairy() { return allergyDairy; }
    public void setAllergyDairy(boolean allergyDairy) { this.allergyDairy = allergyDairy; }

    public boolean isAllergyGluten() { return allergyGluten; }
    public void setAllergyGluten(boolean allergyGluten) { this.allergyGluten = allergyGluten; }

    public boolean isAllergyEgg() { return allergyEgg; }
    public void setAllergyEgg(boolean allergyEgg) { this.allergyEgg = allergyEgg; }

    public boolean isAllergySeafood() { return allergySeafood; }
    public void setAllergySeafood(boolean allergySeafood) { this.allergySeafood = allergySeafood; }

    public boolean isVegetarian() { return vegetarian; }
    public void setVegetarian(boolean vegetarian) { this.vegetarian = vegetarian; }

    public boolean isVegan() { return vegan; }
    public void setVegan(boolean vegan) { this.vegan = vegan; }

    public boolean isGlutenFree() { return glutenFree; }
    public void setGlutenFree(boolean glutenFree) { this.glutenFree = glutenFree; }

    public boolean isLactoseFree() { return lactoseFree; }
    public void setLactoseFree(boolean lactoseFree) { this.lactoseFree = lactoseFree; }

    public UserProfile getProfile() { return profile; }
    public void setProfile(UserProfile profile) { this.profile = profile; }

    public List<String> toSpoonacularTags() {
        List<String> tags = new ArrayList<>();

        if (vegan) {
            tags.add("vegan");
        } else if (vegetarian) {
            tags.add("vegetarian");
        }

        if (glutenFree || allergyGluten) {
            tags.add("gluten free");
        }

        if (lactoseFree || allergyDairy) {
            tags.add("dairy free");
        }

        if (allergyPeanut) {
            tags.add("peanut");
        }

        if (allergyEgg) {
            tags.add("egg");
        }

        if (allergySeafood) {
            tags.add("seafood");
        }

        return tags;
    }
}
