package actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;
import utils.Path;

public final class ActionsMap {
    private static final Logger LOG = LogManager.getLogger();

    private static final Map<String, Action> actions = new HashMap<>();
    private static final List<String> authenticatedActions = new ArrayList<>();
    private static final Map<String, String> actionsPages = new HashMap<>();

    static {
        // Interfaces Action
        actions.put("GET/invalid", new InvalidAction());
        actions.put("GET/about", (request, response) -> Path.PAGE_ABOUT);
        actions.put("GET/logout", new LogoutAction());
        actions.put("GET/home", new HomePageAction());
        actions.put("GET/show_story", new ShowStoryAction());
        actions.put("GET/show_user_stories", new ShowUserStoriesAction());
        actions.put("GET/show_paragraphe", new ShowParagrapheAction());
        actions.put("GET/read_story", new ReadStoryAction());
        actions.put("GET/remove_invited", new RemoveInvitedAction());

        actions.put("GET/login", (request, response) -> Path.PAGE_LOGIN); // PRG pattern
        actions.put("POST/login", new LoginAction());
        actions.put("GET/register", (request, response) -> Path.PAGE_REGISTER);
        actions.put("POST/register", new RegisterAction());
        actions.put("GET/create_story", (request, response) -> Path.PAGE_CREATE_STORY); // PRG pattern
        actions.put("POST/create_story", new CreateStoryAction());
        actions.put("GET/edit_paragraphe", new EditParagrapheGetAction());
        actions.put("POST/edit_paragraphe", new EditParagraphePostAction());
        actions.put("GET/update_password", (request, response) -> Path.PAGE_UPDATE_PASSWORD);
        actions.put("POST/update_password", new UpdatePasswordAction());
        actions.put("GET/invite_users", new InviteUsersGetAction());
        actions.put("POST/invite_users", new InviteUsersPostAction());

        // Pages source des actions pour la redirection lors de la validation
        actionsPages.put("POST/login", Path.PAGE_LOGIN);
        actionsPages.put("POST/register", Path.PAGE_REGISTER);

        authenticatedActions.add("edit_paragraphe");
        authenticatedActions.add("create_story");
        authenticatedActions.add("invite_users");
        authenticatedActions.add("update_password");
        authenticatedActions.add("show_user_stories");
        authenticatedActions.add("remove_invited");

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of actions --> " + actions.size());
    }

    private ActionsMap() {}

    public static boolean authenticatedActionsContains(String actionName) {
        return authenticatedActions.contains(actionName);
    }

    public static Action get(HttpServletRequest request) {
        String actionName = request.getMethod() + "/" + request.getParameter("action");
        LOG.error("Action: " + actionName);

        if (request.getParameter("action").isEmpty())
            return actions.get("GET/about");
        if (!actions.containsKey(actionName)) {
            LOG.trace("Command not found, name --> " + actionName);
            return actions.get("GET/invalid");
        }

        return actions.get(actionName);
    }

    public static String get(String actionName) {
        return actionsPages.get(actionName);
    }

    public static boolean contains(String actionName) {
        return actions.get(actionName) != null;
    }
}
