package actions;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;
import utils.Path;

public class ActionsMap {
    private static final Logger LOG = LogManager.getLogger();

    private static final Map<String, Action> actions = new HashMap<>();

    static {
        actions.put("GET/invalid", new InvalidAction());
        actions.put("GET/about", (request, response) -> Path.PAGE_ABOUT);
        actions.put("POST/login", new LoginAction());
        actions.put("GET/login", (request, response) -> Path.PAGE_LOGIN);
        actions.put("GET/logout", new LogoutAction());
        actions.put("POST/register", new RegisterAction());
        actions.put("POST/create_story", new CreateStoryAction());
        actions.put("GET/home", new HomePageAction());
        actions.put("GET/show_user_stories", new ShowUserStoriesAction());
        actions.put("GET/show_story", new ShowStoryAction());

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of actions --> " + actions.size());
    }

    private ActionsMap () {}

    public static Action get(HttpServletRequest request) {
        String actionName = request.getMethod() + "/" + request.getParameter("action");
        LOG.error("Action: " + actionName);

        if (actionName.isEmpty())
            return actions.get("GET/about");
        if (!actions.containsKey(actionName)) {
            LOG.trace("Command not found, name --> " + actionName);
            return actions.get("GET/invalid");
        }

        return actions.get(actionName);
    }

    public static boolean contains(String actionName) {
        return actions.get(actionName) != null;
    }

}
