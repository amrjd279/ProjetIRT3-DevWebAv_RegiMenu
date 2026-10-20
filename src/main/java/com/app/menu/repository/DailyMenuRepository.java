package com.app.menu.repository;

import com.app.menu.entity.DailyMenu;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Contrat de persistance pour les menus quotidiens.
 * Les implémentations doivent utiliser Spring JdbcClient (pas de JPA).
 */
public interface DailyMenuRepository {
 // Trouve le menu le plus récent pour un utilisateur donné à une date spécifique.
    Optional<DailyMenu> findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(LocalDate menuDate, Long userProfileId);
 //Trouve le menu le plus récent pour un utilisateur donné à une date spécifique mais pour un utilisateur anonyme (userProfileId = null).
    Optional<DailyMenu> findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc(LocalDate menuDate);

    DailyMenu save(DailyMenu dailyMenu);
}
