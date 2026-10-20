package com.app.profile.repository;

import com.app.profile.entity.UserProfile;

import java.util.List;
import java.util.Optional;

/**
 * Contrat de persistance pour les profils utilisateurs.
 * Les requêtes SQL sont écrites avec Spring JdbcClient (pas de JPA conformément au cahier des charges).
 */
public interface UserProfileRepository {

    Optional<UserProfile> findById(Long id);

    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByUsername(String username);

    List<UserProfile> findAll();

    UserProfile save(UserProfile userProfile);
}
