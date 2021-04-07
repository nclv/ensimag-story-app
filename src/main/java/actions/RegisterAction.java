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
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class RegisterAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = -9085228547761887838L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("Register Action starts");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        password = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = User.builder().name(username).password(password).build();

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            UserDAO userDAO = daoFactory.getUserDAO();

            long userId = userDAO.saveUser(user);
            LOG.error(userId + " " + user);
            LOG.trace("User with id=" + userId + " was saved to the DB");

            return true;
        });
        if (result.isEmpty()) {
            // Database error: already used username or another SQLException
            LOG.error("You need to change your username --> [" + username + "]");

            request.setAttribute("error_message", ErrorMessage.get("username_used"));
            return Path.PAGE_REGISTER;
        }

        LOG.info("User [username --> [" + username + "], [password --> " + password + "] successfully signed up");
        LOG.debug("Register Action finished");

        return Path.REDIRECT_LOGIN;
    }

}
