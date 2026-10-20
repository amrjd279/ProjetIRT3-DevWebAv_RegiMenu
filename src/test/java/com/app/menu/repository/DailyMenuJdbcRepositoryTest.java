package com.app.menu.repository;

import com.app.menu.entity.DailyMeal;
import com.app.menu.entity.DailyMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests d'intégration de la couche persistance avec Spring JdbcClient.
 * (Section 4.1 & 4.3 du cahier des charges).
 */
@SpringBootTest
@Transactional // Annule les modifications en BDD à la fin de chaque test
class DailyMenuJdbcRepositoryTest {

    @Autowired
    private DailyMenuRepository dailyMenuRepository;

    /**
     * Test 1 (Exemple 1/3 - Fonctionnel) :
     * Vérifie la persistance d'un menu complet avec ses 3 repas pour un utilisateur identifié.
     */
    @Test
    @DisplayName("Devrait sauvegarder et recharger un menu complet avec ses 3 repas")
    void shouldSaveAndRetrieveDailyMenuWithMeals() {
        // GIVEN : un menu du jour avec 3 repas pour l'utilisateur ID 10
        LocalDate today = LocalDate.now();
        Long userId = 10L;

        DailyMenu menu = new DailyMenu(userId, today);
        List<DailyMeal> meals = List.of(
                new DailyMeal(101L, "Salade de quinoa", "https://img.com/101.jpg", 15, true, true),
                new DailyMeal(102L, "Poulet rôti aux herbes", "https://img.com/102.jpg", 40, false, false),
                new DailyMeal(103L, "Soupe de potimarron", "https://img.com/103.jpg", 25, true, true)
        );
        menu.setMeals(meals);

        // WHEN : sauvegarde via JdbcClient
        DailyMenu saved = dailyMenuRepository.save(menu);

        // THEN : le menu et ses repas doivent être persistés avec des IDs auto-générés
        assertThat(saved.getId()).isNotNull();

        // AND : rechargement depuis la base H2
        Optional<DailyMenu> reloadedOpt = dailyMenuRepository
                .findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(today, userId);

        assertThat(reloadedOpt).isPresent();
        DailyMenu reloaded = reloadedOpt.get();
        assertThat(reloaded.getUserProfileId()).isEqualTo(userId);
        assertThat(reloaded.getMenuDate()).isEqualTo(today);
        assertThat(reloaded.getMeals()).hasSize(3);
        assertThat(reloaded.getMeals().get(0).getTitle()).isEqualTo("Salade de quinoa");
        assertThat(reloaded.getMeals().get(0).getExternalRecipeId()).isEqualTo(101L);
    }

    /**
     * Test 2 (Placeholder Étudiant 2/3 - À toi de jouer !) :
     * 
     * Objectif : Vérifier qu'un menu généré pour un VISITEUR ANONYME (userProfileId == null)
     * est correctement sauvegardé et retrouvé par `findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc`.
     * 
     * Consigne :
     * 1. Crée un `DailyMenu` avec `null` en premier argument du constructeur (userProfileId).
     * 2. Ajoute au moins 1 plat dans la liste de repas.
     * 3. Appelle `dailyMenuRepository.save(menu)`.
     * 4. Récupère le menu via `dailyMenuRepository.findTopByMenuDateAndUserProfileIdIsNullOrderByGeneratedAtDesc(today)`.
     * 5. Vérifie avec `assertThat(...)` que le menu est présent et que son `getUserProfileId()` est bien null.
     */
    @Test
    @DisplayName("Devrait sauvegarder et recharger un menu pour un visiteur anonyme")
    void studentTask_shouldSaveAndRetrieveMenuForAnonymousVisitor() {
        // TODO: ÉTUDIANT - Complète ce test en suivant les consignes ci-dessus !
        // LocalDate today = LocalDate.now();
        // DailyMenu visitorMenu = new DailyMenu(null, today);
        // ...
    }

    /**
     * Test 3 (Placeholder Étudiant 3/3 - À toi de jouer !) :
     * 
     * Objectif : Vérifier que si aucun menu n'a été généré pour une date donnée (ex: hier),
     * la recherche renvoie un Optional vide (`Optional.empty()`).
     * 
     * Consigne :
     * 1. Choisis une date passée : `LocalDate yesterday = LocalDate.now().minusDays(1);`
     * 2. Appelle `dailyMenuRepository.findTopByMenuDateAndUserProfileIdOrderByGeneratedAtDesc(yesterday, 999L);`
     * 3. Vérifie avec `assertThat(resultOpt).isEmpty();`.
     */
    @Test
    @DisplayName("Devrait renvoyer un Optional vide si aucun menu n'existe pour la date demandée")
    void studentTask_shouldReturnEmptyWhenNoMenuExistsForDate() {
        // TODO: ÉTUDIANT - Complète ce test en suivant les consignes ci-dessus !
    }
}
