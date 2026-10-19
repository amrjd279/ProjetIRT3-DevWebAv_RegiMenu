package com.app.profile.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entité représentant le profil d'un utilisateur et ses préférences.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Embedded
    private DietaryPreferences preferences = new DietaryPreferences();

    // Compteur de requêtes API consommées par cet utilisateur (pour Admin / v0.3)
    private int requestCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public UserProfile() {}

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
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
        return preferences;
    }

    public void setPreferences(DietaryPreferences preferences) {
        this.preferences = preferences;
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

