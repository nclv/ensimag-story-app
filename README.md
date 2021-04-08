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

Les *filtres* font usage du patron de conception *Chain of Responsability* lors de l'appel à la fonction *doFilter()*. Nous utilisons deux filtres spécifiques: un pour l'encodage des pages en UTF-8 et un pour filtrer l'accès aux pages réservées aux utilisateurs identifiés. Les autres filtres sont des filtres de validation. Ils vérifient que les paramètres de la requêtes sont corrects avant de réaliser les actions.

# Resources
- [web.xml init-param/context-param](https://stackoverflow.com/a/28393315)
- [login servlet](https://stackoverflow.com/a/13276996)
- [servlets info](https://stackoverflow.com/tags/servlets/info)