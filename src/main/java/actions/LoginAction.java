package actions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.Path;

public class LoginAction implements Action {
    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = -7377459693380607305L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("Login Action starts");

        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // Validation requête
        if (username == null || username.isEmpty()) {
            LOG.error("There is no username --> [" + username + "]");

            request.setAttribute("error_message", "Enter an user name.");
            return Path.PAGE_LOGIN;
        } else if (password == null || password.isEmpty()) {
            LOG.error("There is no password --> [" + username + "]");

            request.setAttribute("error_message", "Enter a password.");
            return Path.PAGE_LOGIN;
        }

        UserDAO userDao = new UserDAOimpl();
        User user = userDao.findUser(username);

        // Validation database
        String forward = Path.PAGE_HOME;
        if (user == null) {
            LOG.error("There is no such a username --> [" + username + "]");

            request.setAttribute("error_message", username + " is invalid.");
            forward = Path.PAGE_LOGIN;
        } else if (!password.equals(user.getPassword())) {
            LOG.error("Incorrect password --> " + password + " for [" + username + "]");

            request.setAttribute("error_message", username + " exist but invalid password.");
            forward = Path.PAGE_LOGIN;
        } else {
            LOG.trace("User [username --> '" + username + "', password --> '" + password + "'] successfully signed in");

            session.setAttribute("user", user);
        }

        LOG.debug("Login Action finished");

        return forward;
    }
}
