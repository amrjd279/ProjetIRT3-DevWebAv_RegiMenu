package com.app.profile.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Profil utilisateur.
 *
 * US4.1 :
 * Le profil est stocké dans la table user_profiles.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    /**
     * Un profil possède un seul ensemble de préférences.
     *
     * CascadeType.ALL :
     * lorsqu'on sauvegarde le profil, les préférences sont également sauvegardées.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private DietaryPreferences preferences;

    /**
     * Compteur déjà utilisé par le projet pour suivre les requêtes.
     */
    private int requestCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Constructeur obligatoire pour JPA.
     */
    public UserProfile() {
    }

    /**
     * Constructeur pratique pour créer un profil.
     */
    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;

        setPreferences(new DietaryPreferences());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DietaryPreferences getPreferences() {

        /*
         * Si le profil n'a pas encore de préférences,
         * on les crée automatiquement.
         */
        if (preferences == null) {
            setPreferences(new DietaryPreferences());
        }

        return preferences;
    }

    public void setPreferences(DietaryPreferences preferences) {
        this.preferences = preferences;

        /*
         * On maintient les deux côtés de la relation.
         */
        if (preferences != null) {
            preferences.setProfile(this);
        }
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}