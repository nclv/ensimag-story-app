package actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.DatabaseManager;
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

        String redirect = request.getParameter("redirect");
        LOG.error(redirect);

        // Validation requête
        // return to prevent database access
        if (username == null || username.trim().isEmpty()) {
            LOG.error("There is no username --> [" + username + "]");

            request.setAttribute("error_message", "Enter an user name.");
            return Path.PAGE_LOGIN;
        } else if (password == null || password.trim().isEmpty()) {
            LOG.error("There is no password --> [" + username + "]");

            request.setAttribute("error_message", "Enter a password.");
            return Path.PAGE_LOGIN;
        }

        UserDAO userDao = new UserDAOimpl();

        User user = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            UserDAOimpl.setConnection(connection);

            user = userDao.findUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Redirect to previous page or to home page 
        String forward = Path.REDIRECT_HOME;

        // Validation database
        if (user == null) {
            LOG.error("There is no such a username --> [" + username + "]");

            request.setAttribute("error_message", username + " is invalid.");
            return Path.PAGE_LOGIN;
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
            LOG.error("Incorrect password --> " + password + " for [" + username + "]");

            request.setAttribute("error_message", username + " exist but invalid password.");
            return Path.PAGE_LOGIN;
        } else {
            LOG.trace("User [username --> '" + username + "', password --> '" + password + "'] successfully signed in");

            session.setAttribute("user", user);
        }

        // On a passé toutes les validations, forward = Path.REDIRECT_HOME
        // Important de filtrer les espaces !!
        if (redirect != null && !redirect.trim().isEmpty()) {
            forward = redirect;
        }

        LOG.debug("Login Action finished");

        return forward;
    }
}
