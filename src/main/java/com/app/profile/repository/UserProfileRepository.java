package com.app.profile.repository;

import com.app.profile.entity.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository permettant d'accéder à la table user_profiles.
 */
public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {
}