package actions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.Path;

public class LogoutAction implements Action {
    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -6166985392326198420L;
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("Logout Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("error_message", "There is no connected user.");
            return Path.PAGE_ERROR;
        }

        LOG.info("User [username --> [" + user.getName() + "], [password --> " + user.getPassword()
                + "] successfully signed out");

        LOG.trace("Session invalidated");
        session.invalidate();

        LOG.debug("Logout Action finished");

        return Path.REDIRECT_HOME;
    }
}
