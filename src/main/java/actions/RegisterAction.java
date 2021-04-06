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
import models.User;
import utils.DatabaseManager;
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

        UserDAO userDao = new UserDAOimpl();

        long userId = -1;
        try (Connection connection = DatabaseManager.getConnection()) {
            UserDAOimpl.setConnection(connection);

            userId = userDao.saveUser(user);
            LOG.error(userId + " " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Validation database
        String forward = Path.REDIRECT_LOGIN;
        if (userId == -1) {
            LOG.error("You need to change your username --> [" + username + " " + userId + "]");

            request.setAttribute("error_message", username + " is already used.");
            forward = Path.PAGE_REGISTER;
        }

        LOG.trace("User with id=" + userId + " was saved to the DB");
        LOG.info("User [username --> [" + username + "], [password --> " + password + "] successfully signed up");
        LOG.debug("Register Action finished");

        return forward;
    }

}
