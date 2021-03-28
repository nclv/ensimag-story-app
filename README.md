## Design Patterns

Dans notre application, l'utilisation du *pattern* MVC se fait à plusieurs échelles:
- au niveau de l'architecture globale,
  - **M**: les interfaces *DAOs* (notre *Business Model*),
  - **V**: le code HTML des pages JSP,
  - **C**: les *servlets* (*Controller* et *Home* implémentés dans notre application) ainsi que les tags JSTL.
- au niveau du développeur,
  - **M**: les entités représentant le schéma SQL de la *database* (notre *Data Model*) et les implémentations des *DAOs*,
  - **V**: les pages JSP,
  - **C**: les actions dispatchées par le *Controller*,
- au niveau de l'utilisateur,
  - **M**: /,
  - **V**: le rendu HTML des pages JSP,
  - **C**: le navigateur,

Nous avons choisi d'utiliser le *Front Controller Pattern*. Nous utilisons donc un unique *servlet*, nommé *Controller*, qui sert de point d'accès à toutes (ou presque) les requêtes. Il exécute l'*Action* passée en paramètre et redirige vers la page suivante en utilisant le *Post/Redirect/Get Pattern* qui évite la re-soumission d'un formulaire après rechargement de la page.

Les *Actions* suivent le *Strategy Pattern*. Les classes concrètes représentant les actions implémentent la méthode *execute* de l'interface *Action*. La classe *ActionsMap* implémente le *Factory Method Pattern* en nous permettant de récupérer les implémentations de l'interface *Action* selon le protocole de la requête et le nom de l'action.

Les *filtres* font usage du patron de conception *Chain of Responsability* lors de l'appel à la fonction *doFilter()*. Nous utilisons deux filtres: un pour l'encodage des pages en UTF-8 et un pour filtrer l'accès aux pages réservées aux utilisateurs identifiés.

# Resources
- [web.xml init-param/context-param](https://stackoverflow.com/a/28393315)
- [login servlet](https://stackoverflow.com/a/13276996)
- [servlets info](https://stackoverflow.com/tags/servlets/info)