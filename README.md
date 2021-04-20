# Installation

Dans notre choix de nos outils de développement, nous avons souhaité les dernières versions stables de Tomcat et Java, à savoir:
- Apache Tomcat 10.0.2,
- JavaSE-14,

Vous allez surement rencontrer des problèmes si vous n'utilisez pas cette version du serveur Tomcat (voir la documentation Tomcat). Il doit être possible d'utiliser Java 8 mais pas les versions antérieures (utilisations des streams).

Après avoir configuré le serveur Tomcat, installez le projet avec Maven, chargez `story-app.war` sur le serveur et consultez le site.

# L'application
## Design Patterns

Dans notre application, l'utilisation du *pattern* MVC se fait à plusieurs échelles:
- au niveau de l'architecture globale,
  - **M**: les interfaces des *DAOs* (notre *Business Model*),
  - **V**: le code HTML des pages JSP,
  - **C**: les *filtres* (`/filters`) et *servlets* (`/servlets`) ainsi que les tags JSTL.
- au niveau du développeur,
  - **M**: les entités (*Beans*) représentant le schéma SQL de la *database* (notre *Data Model*) et les implémentations des *DAOs*,
  - **V**: les pages JSP,
  - **C**: les actions (`/actions`) dispatchées par le *Controller*,
- au niveau de l'utilisateur,
  - **M**: /,
  - **V**: le rendu HTML des pages JSP,
  - **C**: le navigateur,

Nous avons choisi d'utiliser le *Front Controller Pattern*. Nous utilisons donc un unique *servlet*, nommé *Controller*, qui sert de point d'accès à toutes (ou presque) les requêtes. Il exécute l'*Action* passée en paramètre et redirige vers la page suivante en utilisant le *Post/Redirect/Get Pattern* qui évite la re-soumission d'un formulaire après rechargement de la page.

Les *Actions* suivent le *Strategy Pattern*. Les classes concrètes représentant les actions implémentent la méthode *execute* de l'interface *Action*. La classe *ActionsMap* implémente le *Factory Method Pattern* en nous permettant de récupérer les implémentations de l'interface *Action* selon le protocole de la requête et le nom de l'action.

Nous utilisons le pattern *Data Access Object* (DAO). Nous avons donc des interfaces regroupant les opérations sur un modèle et leur implémentation pour le SGBD Oracle. Les DAOs sont accessibles par le pattern *Factory*. La classe *DAOManager* fait à la fois office de *Factory* pour les DAOs et de *Manager* - gestion des erreurs lors des opérations sur la BDD (query, transaction, rollback) - pour le SGBD Oracle. Elle aurait pu être nommée *OracleDAOManager*.

Les *filtres* font usage du patron de conception *Chain of Responsability* lors de l'appel à la fonction *doFilter()*. Nous utilisons deux filtres spécifiques: un pour l'encodage des pages en UTF-8 et un pour filtrer l'accès aux pages réservées aux utilisateurs identifiés. Les autres filtres sont des filtres de validation. Ils vérifient que les paramètres de la requête sont corrects avant de réaliser les actions.

## Modules

- La gestion des messages d'erreurs est déléguée à la classe `ErrorMessage`. 
- Les fonctions de validation sont organisés dans la classe `Validation`. On peut distinguer deux types de validation. Ce qu'on appelle la pré-validation, ie. la validation des paramètres de la requête obtenue des pages JSP, et la post-validation, ie. les étapes de validation effectuées après requête à la BDD.
- Les points d'entrées de l'application sont codés dans la classe `Path`.
- Le servlet `Controller` s'occupe de l'exécution des actions. Le servlet `Home` s'occupe de la redirection de la racine `/`.
- Les `Action`s sont stockées dans la classe `ActionsMap`.
- Les champs de la database sont stockées dans la classe `DatabaseFields`.
- La classe `DAOManager` sert à la fois de factory et de manager en donnant accès à des méthodes génériques de gestion de la BDD.
- La classe `ErrorHandler` redirige toutes les erreurs vers la page correspondante.

---

- Le dossier `/jsp/authenticated_user` contient les *views* qui ne sont accessibles que par un utilisateur identifié. 
- Les *views* qui ne sont accessibles que via le controller sont dans le dossier `WEB-INF`.
- Le fichier `utils.js` contient une fonction permettant la duplication d'une balise HTML et de son contenu. Elle est utilisé pour l'ajoût de choix.

## URLs

Method | URL | Action
--- | --- | ---
GET | `/`  | home
GET | `/controller?action=home` | HomePage
GET | `/controller?action=register` | -
POST | `/controller?action=register` | Register
GET | `/controller?action=login` | -
POST | `/controller?action=login` | Login
GET | `/controller?action=logout` | Logout
GET | `/controller?action=update_password` | -
POST | `/controller?action=update_password` | UpdatePassword
||
GET | `/controller?action=create_story` | -
POST | `/controller?action=create_story` | CreateStory
GET | `/controller?action=publish_story` | PublishUnpublishStory
GET | `/controller?action=read_story` | ReadStory
GET | `/controller?action=show_story` | ShowStory
GET | `/controller?action=show_user_stories` | ShowUserStories
||
GET | `/controller?action=show_paragraphe` | ShowParagraphe
GET | `/controller?action=edit_paragraphe` | EditParagrapheGet
POST | `/controller?action=edit_paragraphe` | EditParagraphePost
GET | `/controller?action=unlock_paragraphe` | UnlockParagraphe
GET | `/controller?action=remove_paragraphe` | RemoveParagraphe
||
GET | `/controller?action=invite_users` | InviteUsersGet
POST | `/controller?action=invite_users` | InviteUsersPost
GET | `/controller?action=remove_invited` | RemoveInvited
||
GET | `/controller?action=clear_history` | ClearHistory
GET | `/controller?action=save_history` | SaveHistory

## Fonctionnalités

### Historique

L'historique est implémenté comme demandé dans le cahier des charges. Il est possible de vider l'historique et de le sauvegarder. Le chargement de l'historique sauvegardé (s'il existe) s'effectue après une suppression de l'historique actuel.
Chaque session a un unique historique. Si l'utilisateur décide de lire une autre *story* alors son historique actuel est vidé. Il est donc conseillé de sauvegarder son historique avant d'entamer la lecture d'une autre histoire.

### Paragraphes en cours d'édition

Un utilisateur connecté ne peut éditer qu'un unique paragraphe sur tout le site. S'il commence l'édition d'un paragraphe sans valider le formulaire alors ce paragraphe apparaîtra en vert. Par ailleurs, il n'est pas possible d'éditer un paragraphe en cours d'édition par un autre utilisateur. Ces paragraphes apparaîtront en rouge.

# TODOs.

Nous n'avons pas implémenté les choix conditionnels. Il est possible de choisir un choix conditionnel lorsque l'on édite un paragraphe mais cela n'a aucun impact sur la *story*. Toutes les autres fonctionnalités du CdCF sont implémentées, ainsi que quelques compléments. Il reste cependant un gros travail de refactoring à effectuer sur la validation notamment (fonctions non ordonnées, trop d'appels à la BDD, ...).

**L'ajoût d'un choix à la fin d'un paragraphe par un autre utilisateur est possible si et seulement si l'auteur du paragraphe a déverrouillé ce paragraphe.**

## Refactoring

- [ ] Remplacer `SELECT *` par les *fields* de la table. Utiliser `DatabaseFields` dans les requêtes SQL plutôt que de coder en dur.
- [ ] Ajouter des DAOs pour les jointures et remplacer le code Java.
- [ ] Créer des *views* spécifiques (objets envoyés au client pour l'affichage), pour Paragraphe notamment. Pour le moment nous utilisons les modèles pour construire les pages JSP. Il faudrait créer des structures spécifiques (*views*) pour chaque page. Ces objets seraient ensuite retournées directement par les fonctions du `DAOManager`. Il serait alors possible de sortir la logique de validation et le `setAttributes` de ces fonctions (ce n'est pas leur place).
- [ ] Revoir les filtres: corriger les redirections vers le login, organiser les fonctions pre/post validation dans `Validation`, supprimer les filtres inutiles.

## Fonctionnalités de base

- [x] Enregistrement, login et logout d'utilisateurs,
- [x] Création d'un histoire ouverte/fermée, publié/non publiée,
- [x] Publication/dé-publication d'une histoire,
- [x] Lecture d'une histoire,
- [x] Affichage de son historique de lecture,
- [x] Sauvegarde de son historique,
- [x] Suppression de son historique,
- [x] Affichage d'une histoire (paragraphes + auteurs), 
- [x] Affichage des paragraphes en cours d'édition par un autre utilisateur,
- [x] Affichage du paragraphe que l'on est en train d'éditer,
- [x] Déverrouillage de l'un de ses paragraphes,
- [x] Suppression de l'un de ses paragraphes,
- [x] Inviter/désinviter un ou plusieurs utilisateurs sur son histoire fermée,
- [x] Modifier un paragraphe (finalité + contenu + choix),
- [x] Convergence de l'histoire,
- [ ] Choix conditionnels.

## Fonctionnalités supplémentaires

- [x] **Hash and salt** the passwords,
- [x] Update password,
- [x] **Redirection vers la page de login puis vers la page initialement demandée**,
- [x] **Autocomplétion des champs des formulaires** avec les informations précédemment rentrées si erreur de validation,
- [x] Pattern Post/Redirect/Get, 
- [x] Affichage des histoires (créé + invité) d'un utilisateur connecté,
- [x] Chargement automatique de son historique,
- [ ] Demander à un autre utilisateur l'autorisation d'éditer un paragraphe lui appartenant,
- [ ] Script SQL pour peupler la BDD,

## Tests

*Aucun test automatique n'est effectué :(*.

# Resources
- [web.xml init-param/context-param](https://stackoverflow.com/a/28393315)
- [login servlet](https://stackoverflow.com/a/13276996)
- [servlets info](https://stackoverflow.com/tags/servlets/info)