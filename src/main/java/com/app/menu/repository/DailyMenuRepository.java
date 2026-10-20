package com.app.menu.repository;

import com.app.menu.entity.DailyMenu;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Contrat de persistance pour les menus quotidiens.
 * Les implémentations doivent utiliser Spring JdbcClient (pas de JPA).
 */
public interface DailyMenuRepository {

    Optional<DailyMenu> findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(LocalDate menuDate, Long userProfileId);

    Optional<DailyMenu> findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc(LocalDate menuDate);

    DailyMenu save(DailyMenu dailyMenu);
}
