package com.app.profile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Préférences alimentaires d'un utilisateur.
 *
 * US4.1 :
 * Cette classe est une véritable entité JPA.
 *
 * Elle correspond donc à la table :
 *
 * user_preferences
 */
@Entity
@Table(name = "user_preferences")
public class DietaryPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Préférence végétarienne.
     */
    private boolean vegetarian = false;

    /**
     * Préférence végane.
     */
    private boolean vegan = false;

    /**
     * Régime sans gluten.
     */
    private boolean glutenFree = false;

    /**
     * Régime sans lactose.
     */
    private boolean lactoseFree = false;

    /**
     * Profil auquel appartiennent ces préférences.
     *
     * mappedBy = "preferences"
     * signifie que UserProfile possède la relation principale.
     */
    @OneToOne(mappedBy = "preferences")
    private UserProfile profile;

    /**
     * Constructeur obligatoire pour JPA.
     */
    public DietaryPreferences() {
    }

    /**
     * Constructeur pratique.
     */
    public DietaryPreferences(
            boolean vegetarian,
            boolean vegan,
            boolean glutenFree,
            boolean lactoseFree
    ) {
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.lactoseFree = lactoseFree;
    }

    public Long getId() {
        return id;
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

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * Transforme les préférences en tags compatibles avec Spoonacular.
     *
     * Exemple :
     *
     * vegan = true
     * glutenFree = true
     *
     * donne :
     *
     * ["vegan", "gluten free"]
     */
    public List<String> toSpoonacularTags() {

        List<String> tags = new ArrayList<>();

        /*
         * Vegan est plus restrictif que végétarien.
         *
         * Si vegan est activé, on envoie seulement "vegan".
         */
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