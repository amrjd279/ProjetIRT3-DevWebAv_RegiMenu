# Cahier des Charges & Spécifications - RegiMenuApp

---

## 1. Présentation du Projet

### 1.1 Qu'est-ce que RegiMenuApp ?
**RegiMenuApp** est une application web intelligente de planification de repas personnalisée. Elle combine :
* Une interface utilisateur moderne et réactive développée en **React**.
* Un backend modulaire et robuste en **Java 17+ / Spring Boot 3.2+**, architecturé selon les principes de **Spring Modulith**.
* Une base de données relationnelle **PostgreSQL** opérée via **Spring `JdbcClient`** (requêtes SQL explicites et paramétrées, sans ORM lourd / pas de JPA).
* Une intégration de l'API externe **Spoonacular** pour les données de recettes via une couche Anti-Corruption (ACL).
* Une gestion sécurisée des accès, de l'authentification et des rôles via **Spring Security**.
* Un hébergement dans le cloud sur la plateforme **Render** avec prévisualisation des Pull Requests (PR previews).

### 1.2 Origine du nom "RegiMenuApp"
* **Régime / Régulation :** prise en compte stricte des régimes (végétarien, végan, sans gluten, sans lactose), allergies et objectifs alimentaires.
* **Menu :** génération et ordonnancement quotidien de suggestions de repas équilibrées.
* **App :** application web ergonomique pensée pour simplifier la charge mentale des repas.

### 1.3 Architecture des Modules Métier (Spring Modulith)
L'application est construite sous la forme d'un **Monolithe Modulaire** dont l'étanchéité et les dépendances sont vérifiées automatiquement par **Spring Modulith** :
1. **`clientapi`** : passerelle réseau externe (Spoonacular) et couche anti-corruption (ACL) pour mapper les données tierces.
2. **`menu`** : cœur métier de gestion du menu du jour, régénération des plats, validation et historique des repas.
3. **`profile`** : gestion du profil utilisateur et de ses préférences/contraintes alimentaires.
4. **`favorite`** : gestion de la bibliothèque de recettes coups de cœur de l'utilisateur.
5. **`admin`** : supervision globale de la plateforme, gestion des comptes et audit de consommation de l'API externe.
6. **`security` (transversal)** : authentification, gestion des mots de passe, rôles (`ROLE_USER`, `ROLE_ADMIN`) et protection des endpoints via **Spring Security**.

---

## 2. Profils d'Utilisateurs & Parcours Fonctionnels

### 2.1 Visiteur (Utilisateur non connecté)
* Accède à la page d'accueil et visualise immédiatement les **3 repas suggérés du jour** (persistants au rafraîchissement).
* Peut consulter la fiche détaillée complète de n'importe quel plat (ingrédients, temps, instructions étape par étape).
* **Restriction clé :** le visiteur **ne peut pas régénérer** un nouveau menu ni enregistrer de favoris ; le bouton d'action l'invite à se connecter ou à créer un compte.

### 2.2 Utilisateur Connecté (`ROLE_USER`)
* S'authentifie et reste connecté via **Spring Security**.
* Visualise son menu du jour personnalisé en fonction de ses **préférences alimentaires et allergies**.
* Peut déclencher le bouton **"Régénérer d'autres repas"** pour recalculer une suggestion sur-mesure.
* Dispose d'un module **"Mon Profil & Préférences"** pour ajuster ses régimes (végétarien, sans gluten, végan, etc.) et allergies (arachides, œufs, lait, fruits de mer, gluten) à tout moment.
* Dispose d'un module **"Favoris"** pour mettre en favori ses recettes coups de cœur en base de données.
* Peut **valider définitivement son menu du jour** et consulter la page **"Historique"** pour revoir les repas passés.

### 2.3 Administrateur (`ROLE_ADMIN`)
* Accède à un tableau de bord spécifique (`/admin`) protégé par Spring Security.
* Surveille le volume global et individuel de requêtes API Spoonacular consommées (`requestCount`) pour maîtriser les coûts et quotas.
* Peut auditer, suspendre ou supprimer des comptes utilisateurs en cas d'abus.

---

## 3. Architecture Technique & Choix Technologiques

| Domaine | Technologie retenue | Justification / Contrainte |
| :--- | :--- | :--- |
| **Architecture** | Monolithe Modulaire (**Spring Modulith**) | Isolation forte des sous-domaines métier, vérification automatisée de l'architecture par tests. |
| **Backend** | Java 17+, Spring Boot 3.2+ | Exposition de l'API REST via contrôleurs dédiés, utilisation de Spring `RestClient`. |
| **Persistance** | **Spring `JdbcClient` (pas de JPA)** | **Obligatoire au cahier des charges** : requêtes SQL paramétrées et maîtrisées, pas de mapping ORM opaque. |
| **Base de Données** | **PostgreSQL** (Prod) / **H2** (Dev & Tests) | Base relationnelle robuste avec script d'initialisation portable `schema.sql`. |
| **Authentification** | **Spring Security** | Sécurisation des routes REST, hachage BCrypt des mots de passe, gestion de session / JWT et RBAC (`USER` / `ADMIN`). |
| **Frontend** | React (Vite) | Single Page Application (SPA) réactive, composants modulaires et responsive. |
| **API Externe** | Spoonacular API | Source de données de recettes avec couche Anti-Corruption (ACL) étanche. |
| **Déploiement** | PaaS **Render** | Déploiement continu du service web + base PostgreSQL + PR Previews. |
| **Qualité & Tests** | JUnit 5, AssertJ, Spring Modulith Test | Tests unitaires, tests d'intégration `JdbcClient` et tests d'architecture modulaire. |

---

## 4. Plan de Release & Livrables

### Definition of Done (DoD)
Une release est considérée comme terminée uniquement lorsque :
1. Les fonctionnalités prévues sont codées et fonctionnelles de bout en bout (front + back).
2. Les tests automatisés associés sont exécutés avec succès (`mvn test` & `vite build`).
3. Le code est fusionné via Pull Request relue avec convention de commits respectée.
4. L'application est déployée et accessible en ligne sur **Render**.

---

### v0.1 : MVP - Socle Technique & Découverte des Repas (2 séances de 4h) — [Terminé]
**Objectif :** Valider la communication frontend React, backend Spring Boot et API externe Spoonacular.
* **Fonctionnalités livrées :**
  * Initialisation du projet (Spring Boot 3.2+ et React Vite).
  * Composant `clientapi` : `RestClient`, interface `RecipeGateway`, DTOs bruts et `RecipeMapper` (ACL).
  * Composant `menu` : exposition de `GET /api/meals/random` et `GET /api/meals/{id}`.
  * Frontend : structure de navigation (Navbar), page d'accueil avec affichage des 3 cartes de repas, et page de détail de la recette (`/recipe/:id`).

---

### v0.2 : Persistance des Menus, Préférences & Module Favoris (3 séances de 4h) — [Terminé]
**Objectif :** Sortir du mode stateless grâce à SQL / `JdbcClient`, stabiliser le menu au rafraîchissement, filtrer par allergies et persister les favoris.
* **Fonctionnalités livrées :**
  * **Architecture & BDD :** Intégration de Spring Modulith, migration complète de JPA vers Spring `JdbcClient` et création du script `schema.sql` compatible H2 & PostgreSQL.
  * **Persistance du Menu du Jour (`menu`) :**
    * `GET /api/meals/today` : les repas restent identiques au refresh sans consommer de crédit API Spoonacular.
    * `POST /api/meals/regenerate` : recalcul volontaire d'un nouveau menu filtré selon le profil actif.
  * **Module Profil, Préférences & Allergies (`profile`) :**
    * Stockage des régimes alimentaires (végétarien, végan, sans gluten, sans lactose) et des allergies (arachides, lait, gluten, œufs, fruits de mer).
    * Prise en compte de ces filtres et allergies dans les requêtes Spoonacular (`tags`) et affichage visuel des préférences actives sur l'accueil.
  * **Module Favoris (`favorite`) :**
    * Table `favorite_meals`, endpoints REST (`GET`, `POST`, `DELETE /api/favorites`), implémentation `FavoriteJdbcRepository` avec `JdbcClient` et branchement complet frontend (`MealCard`, `Favoris.jsx`, `RecipeDetail.jsx`).
  * **Environnement PostgreSQL & Déploiement Render :**
    * Conteneurisation locale de PostgreSQL via `docker-compose.yml`.
    * Déploiement du backend et de la base PostgreSQL sur la plateforme Render.

---

### v0.3 : Sécurisation Spring Security & Administration (3 séances de 4h) — [Terminé]
**Objectif :** Brancher Spring Security, gérer l'authentification et les rôles, restreindre les droits et doter l'administrateur d'un tableau de bord.
* **Fonctionnalités livrées :**
  * **Authentification Spring Security :**
    * Intégration de `spring-boot-starter-security` en mode Stateless avec JWT et hachage BCrypt.
    * Modèle utilisateur étendu avec `password_hash`, `role` (`ROLE_USER` / `ROLE_ADMIN`) et `status` (`ACTIVE` / `SUSPENDED`).
    * Endpoints d'authentification (`POST /api/auth/register`, `POST /api/auth/login`).
    * Sécurisation des routes : catalogue public pour les visiteurs, routes protégées pour les utilisateurs connectés, et routes d'administration réservées au rôle `ROLE_ADMIN`.
    * Configuration CORS sécurisée et pérenne (support de localhost, Render et Vercel).
    * Frontend : formulaires Connexion / Inscription, gestion du token Bearer et déconnexion.
  * **Module Administration (`admin`) :**
    * Endpoint `GET /api/admin/users` : consultation des utilisateurs et de leur statut.
    * Modération : suspension (`PUT /api/admin/users/{id}/suspend`) et suppression (`DELETE /api/admin/users/{id}`).
    * Tableau de bord frontend `/admin` dédié aux administrateurs.
  * **Tests de Sécurité & Modularité :**
    * Tests d'architecture logicielle Spring Modulith validant l'absence de cycles, l'encapsulation et l'indépendance de `security`.
    * Tests d'intégration Spring Security (MockMvc) vérifiant les codes 401/403, les accès selon les rôles et le blocage des comptes suspendus.

---

### v1.0 : Historique, Validation Finale & Production (2 séances de 4h) — [En cours]
**Objectif :** Finaliser le cycle de vie du menu, consulter l'historique et préparer la livraison finale.
* **Fonctionnalités livrées :**
  * **Cycle de Vie & Validation du Menu du Jour (Backend) :**
    * Extension de la table `daily_menus` avec les colonnes `is_validated` et `validated_at`.
    * Règle métier de verrouillage : interdiction stricte de régénérer un menu validé via l'exception métier `MenuAlreadyValidatedException` (HTTP 409 Conflict).
    * Endpoint `POST /api/meals/today/validate` permettant d'entériner le choix du jour.
    * Suite de tests unitaires `MenuServiceTest` validant l'ensemble des règles de gestion de la validation.
* **Reste à finaliser pour la clôture v1.0 :**
  * **Intégration Frontend du Menu Validé :**
    * Bouton "Valider mon menu" sur la page d'accueil (`Home.jsx`).
    * Verrouillage visuel (désactivation du bouton "Régénérer" et badge "Menu validé").
  * **Module Historique des Menus :**
    * Endpoint backend `GET /api/meals/history` pour l'utilisateur connecté.
    * Interface frontend `Historique.jsx` avec affichage des menus passés par date.
  * **Finalisation & Qualité :**
    * Audit et correction du compteur de requêtes API Spoonacular (`requestCount`).
    * Restriction d'accès à l'onglet Administration aux seuls détenteurs du rôle `ROLE_ADMIN` côté navigation.
    * Documentation finale (README technique et diagrammes C4 exportés).
