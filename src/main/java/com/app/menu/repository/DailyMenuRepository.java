package com.app.menu.repository;

import com.app.menu.entity.DailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {

    Optional<DailyMenu> findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(LocalDate menuDate, Long userProfileId);

    Optional<DailyMenu> findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc(LocalDate menuDate);
}

