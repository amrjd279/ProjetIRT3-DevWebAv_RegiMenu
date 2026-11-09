package com.app.profile.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DietaryPreferencesTest {

    /**
     * Test 1 (Exemple 1/3 - Fonctionnel) :
     * Vérifie que les régimes alimentaires et les allergies sont bien traduits en tags Spoonacular.
     */
    @Test
    @DisplayName("Devrait générer les bons tags Spoonacular pour un profil végétarien avec intolérances")
    void shouldGenerateSpoonacularTagsForDietsAndAllergies() {
        // GIVEN : un utilisateur végétarien avec allergie aux arachides et intolérance au gluten
        DietaryPreferences preferences = new DietaryPreferences();
        preferences.setVegetarian(true);
        preferences.setAllergyPeanut(true);
        preferences.setAllergyGluten(true);

        // WHEN : conversion en tags d'appel Spoonacular
        List<String> tags = preferences.toSpoonacularTags();

        // THEN : les tags attendus doivent être présents
        assertThat(tags).containsExactlyInAnyOrder("vegetarian", "gluten free", "peanut");
    }

    /**
     * Test 2 (Placeholder Étudiant 2/3 - À toi de jouer !) :
     *
     * Objectif : Vérifier la bonne inclusion des allergies aux œufs et fruits de mer.
     *
     * Consigne :
     * 1. Instancie un `DietaryPreferences`.
     * 2. Active `setAllergyEgg(true)` et `setAllergySeafood(true)`.
     * 3. Appelle `preferences.toSpoonacularTags()`.
     * 4. Vérifie que la liste contient "egg" et "seafood".
     */
    @Test
    @DisplayName("Devrait inclure les tags d'allergie egg et seafood")
    void studentTask_shouldIncludeAllergiesForEggAndSeafood() {
        // TODO: ÉTUDIANT - Complète ce test en suivant les consignes ci-dessus !
    }

    /**
     * Test 3 (Placeholder Étudiant 3/3 - À toi de jouer !) :
     *
     * Objectif : Vérifier qu'un profil sans contraintes alimentaires produit une liste de tags vide.
     *
     * Consigne :
     * 1. Instancie un `DietaryPreferences` par défaut (sans rien modifier).
     * 2. Appelle `preferences.toSpoonacularTags()`.
     * 3. Vérifie avec `assertThat(tags).isEmpty()`.
     */
    @Test
    @DisplayName("Devrait renvoyer une liste vide si aucune préférence ni allergie n'est sélectionnée")
    void studentTask_shouldReturnEmptyTagsWhenNoPreferences() {
        // TODO: ÉTUDIANT - Complète ce test en suivant les consignes ci-dessus !
    }
}
