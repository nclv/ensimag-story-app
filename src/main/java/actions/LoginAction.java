package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import dao.DAOManager;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;
import utils.Validation;

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

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            UserDAO userDAO = daoFactory.getUserDAO();

            Optional<User> user = userDAO.findUser(username);

            // Validation database
            boolean valid = true;
            if (user.isEmpty()) {
                LOG.error("There is no such a username --> [" + username + "]");

                request.setAttribute("error_message", ErrorMessage.get("username_invalid"));
                valid = false;
            } else if (!BCrypt.checkpw(password, user.get().getPassword())) {
                LOG.error("Incorrect password --> " + password + " for [" + username + "]");

                request.setAttribute("error_message", ErrorMessage.get("password_invalid"));
                valid = false;
            } else {
                LOG.trace("User [username --> '" + username + "', password --> '" + password
                        + "'] successfully signed in");

                session.setAttribute("user", user.get());
            }
            return valid;
        });
        if (result.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }
        if (!(boolean) result.get()) {
            return Path.PAGE_LOGIN;
        }

        // Redirect to previous page or to home page
        String forward = Path.REDIRECT_HOME;
        if (!Validation.emptyString(redirect)) {
            forward = redirect;
        }

        LOG.debug("Login Action finished");

        return forward;
    }
}
