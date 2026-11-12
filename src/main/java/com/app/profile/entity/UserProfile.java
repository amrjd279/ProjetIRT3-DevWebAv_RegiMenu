package com.app.profile.entity;

import java.time.LocalDateTime;

/**
 * Profil utilisateur.
 * Modèle de domaine sans JPA (persistance assurée via Spring JdbcClient).
 */
public class UserProfile {

    private Long id;

    private String username;

    private String email;

    private DietaryPreferences preferences;

    /**
     * Compteur déjà utilisé par le projet pour suivre les requêtes.
     */
    private int requestCount = 0;

    private UserRole role = UserRole.ROLE_USER;

    private AccountStatus status = AccountStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    public UserProfile() {
        setPreferences(new DietaryPreferences());
    }

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
        setPreferences(new DietaryPreferences());
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
        if (preferences == null) {
            setPreferences(new DietaryPreferences());
        }
        return preferences;
    }

    public void setPreferences(DietaryPreferences preferences) {
        this.preferences = preferences;
        if (preferences != null) {
            preferences.setProfile(this);
        }
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role == null ? UserRole.ROLE_USER : role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status == null ? AccountStatus.ACTIVE : status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
