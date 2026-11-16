package com.app;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests automatisés d'architecture logicielle avec Spring Modulith.
 * (US 12 - Issues #55 & #56, Section 4.2 du cahier des charges).
 */
class ModularityTests {

    private final ApplicationModules modules = ApplicationModules.of(Application.class);

    /**
     * Test 1 (Vérification globale de l'architecture) :
     * Vérifie automatiquement l'absence totale de cycles de dépendances
     * et le respect de l'encapsulation stricte entre tous les composants.
     */
    @Test
    void verifiesModularStructure() {
        // Affiche la cartographie des modules détectés dans la console
        modules.forEach(System.out::println);

        // Lève une exception si une règle d'isolation ou de cycle est violée
        modules.verify();
    }

    /**
     * Test 2 (Détection exhaustive des modules) :
     * Vérifie que les 6 modules métier spécifiés dans le cahier des charges
     * sont bien identifiés par Spring Modulith :
     * - clientapi : passerelle vers Spoonacular et ACL
     * - menu : gestion des menus quotidiens et régénération
     * - profile : préférences alimentaires et profils utilisateurs
     * - favorite : recettes favorites
     * - security : authentification JWT et gestion des rôles
     * - admin : supervision et gestion des comptes
     */
    @Test
    void shouldDetectAllSixBusinessModules() {
        List<String> moduleNames = modules.stream()
                .map(ApplicationModule::getName)
                .toList();

        assertThat(moduleNames)
                .as("L'application doit comporter exactement les 6 modules métier prévus")
                .containsExactlyInAnyOrder("clientapi", "menu", "profile", "favorite", "security", "admin");
    }

    /**
     * Test 3 (Isolation du module transversal Security) :
     * En tant que socle d'authentification, le module security doit rester
     * totalement indépendant des modules métier (menu, favorite, admin, clientapi).
     */
    @Test
    void shouldEnsureSecurityModuleIsIndependentFromBusinessModules() {
        ApplicationModule securityModule = modules.getModuleByName("security")
                .orElseThrow(() -> new AssertionError("Le module 'security' n'a pas été détecté !"));

        List<String> securityDependencies = securityModule.getBootstrapDependencies(modules)
                .map(ApplicationModule::getName)
                .toList();

        assertThat(securityDependencies)
                .as("Le module security ne doit dépendre d'aucun module métier")
                .doesNotContain("menu", "favorite", "admin", "clientapi");
    }

    /**
     * Test 4 (Documentation architecturale automatisée) :
     * Génère automatiquement la documentation technique, la matrice des dépendances
     * et les diagrammes d'architecture C4 dans le dossier target/spring-modulith-docs/.
     */
    @Test
    void createModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
