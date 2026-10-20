package com.app.profile.entity;

import java.time.LocalDateTime;

/**
 * Modèle de domaine représentant le profil d'un utilisateur et ses préférences.
 */
public class UserProfile {

    private Long id;

    private String username;

    private String email;

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

    public void setId(Long id) {
        this.id = id;
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

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
