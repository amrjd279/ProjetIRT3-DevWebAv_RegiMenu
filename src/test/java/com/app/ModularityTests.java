package com.app;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Tests de validation de l'architecture logicielle avec Spring Modulith.
 * (Obligatoire d'après la section 4.2 du cahier des charges).
 */
class ModularityTests {

    ApplicationModules modules = ApplicationModules.of(Application.class);

    /**
     * Test 1 (Exemple 1/3 - Fonctionnel) :
     * Vérifie automatiquement qu'il n'y a pas de dépendance cyclique entre les modules
     * et que l'encapsulation de chaque composant est respectée.
     */
    @Test
    void verifiesModularStructure() {
        // Affiche la structure des modules détectés dans la console
        modules.forEach(System.out::println);

        // Déclenche une erreur si des violations architecturales existent
        modules.verify();
    }

    /**
     * Test 2 (Exemple 2/3 - Fonctionnel) :
     * Génère automatiquement la documentation technique et les diagrammes C4
     * de vos modules dans le dossier target/spring-modulith-docs/ !
     */
    @Test
    void createModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }

    /**
     * Test 3 (Placeholder Étudiant 3/3 - À toi de jouer !) :
     * 
     * Objectif : Vérifier qu'un module spécifique (par exemple "menu") est bien détecté
     * et qu'il expose uniquement les éléments autorisés.
     * 
     * Indice pour l'implémentation :
     * 1. Récupère le module "menu" via modules.getModule("menu").orElseThrow();
     * 2. Utilise org.assertj.core.api.Assertions.assertThat(...) pour vérifier
     *    que le module n'est pas vide et qu'il contient bien tes classes clés (MenuService, etc.).
     */
    @Test
    void studentTask_verifyMenuModuleIsolation() {
        // TODO: ÉTUDIANT - Décommente et complète le code ci-dessous :
        var menuModule = modules.getModuleByName("menu").orElseThrow(() -> new AssertionError("Le module 'menu' n'a pas été détecté !"));
        
        org.assertj.core.api.Assertions.assertThat(menuModule).isNotNull();
        System.out.println("Module menu validé avec succès : " + menuModule.getName());

    }
}

