# Plan de Release et Fonctionnalités


## Définition des fonctionnalités par type d'utilisateur

* Visiteur; il a droit a 3 recettes aléatoires max puis il doit se connecter si il veut relancer.
* Utilisateur connecté: Page d’accueil avec les 3 menus suggérés du jour, il peut cliquer sur un repas pour voir les détails dans une autre page. Il peut refaire générer des menus, il peut modifier son profil et ses préférences ainsi qu’accédé à ses précédents repas.
* L’admin à accès à tout les utilisateurs, leurs nbr de requetes faites, et pouvoir susprendre/supprimer les utiliseurs.

---


## Les critères de fin (Definition of Done) : une release est considérée comme terminée
lorsque : 
* Les fonctionnalités prévues sont opérationnelles ;
* Les tests ont été réalisés ;
* L'application est déployée en ligne.

---
## Plan de Release
### v0.1 : MVP
Après 2-3 séances de 4h.
**Objectif :** Valider la communication entre l'interface, le backend et l'API externe avec des données standards.

* Création de l'interface principale (Frontend) pour la navigation de base.
* Génération et affichage de 3 repas quotidiens (sans préférences ou filtres actifs à ce stade).
* Affichage du détail complet d'une recette (ingrédients, instructions) lors du clic sur un repas.

---

### v0.2 : Personnalisation et Logique Métier
Après 3 séances de 4h.

* Formulaire de connexion et formulaire de création de profil pour le nouvel utilisateur.
* Génération de 3 menus quotidiens, calculés par le système pour respecter les préférences du profil utilisateur.
* Possibilité pour l'utilisateur de cliquer sur un bouton pour regénérer un nouveau menu.

---

### v0.3 : Administration et Flexibilité
Après 3 séances de 4h.

* Une page "Paramètres" permettant à l'utilisateur de modifier ses préférences alimentaires à tout moment.
* Création d'un compte Administrateur disposant d'un tableau de bord spécifique.
* Possibilité pour l'administrateur de surveiller le nbr de requêtes API utilisées par chaque compte 

---

### v1.0 : Historique et Finalisation
Après 2 séances de 4h.
* Possibilité pour l'utilisateur de valider définitivement son menu du jour et donc avoir une page d'accueil dédiee à ce menu
* Création d'une page "Historique" permettant à l'utilisateur de revoir les menus qu'il a validés les jours précédents.