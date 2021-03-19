package actions;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.Path;

public class ActionsMap {
    private static final Logger LOG = LogManager.getLogger();

    private static final Map<String, Action> actions = new HashMap<>();

    static {
        actions.put("invalid", new InvalidAction());
        actions.put("about", (request, response) -> Path.PAGE_ABOUT);
        actions.put("show_login", (request, response) -> Path.PAGE_LOGIN);
        actions.put("login", new LoginAction());
        actions.put("logout", new LogoutAction());

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of actions --> " + actions.size());
    }

    private ActionsMap () {}

    public static Action get(String actionName) {
        if (actionName == null)
            return actions.get("home");
        if (!actions.containsKey(actionName)) {
            LOG.trace("Command not found, name --> " + actionName);
            return actions.get("invalid");
        }

        return actions.get(actionName);
    }

    public static boolean contains(String actionName) {
        return actions.get(actionName) != null;
    }

}
