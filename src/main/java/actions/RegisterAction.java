package actions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;
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

        User user = User.builder().name(username).password(password).build();

        UserDAO userDao = new UserDAOimpl();
        long userId = userDao.saveUser(user);
        LOG.error(userId + " " + user);

        // Validation
        String forward = Path.PAGE_LOGIN;
        if (username == null || username.isEmpty()) {
            LOG.error("There is no username --> [" + username + "]");

            request.setAttribute("error_message", "Enter an user name.");
            forward = Path.PAGE_REGISTER;
        } else if (userId == -1) {
            LOG.error("You need to change your username --> [" + username + " " + userId + "]");

            request.setAttribute("error_message", username + " is already used.");
            forward = Path.PAGE_REGISTER;
        } else if (password == null || password.isEmpty()) {
            LOG.error("There is no password --> [" + username + "]");

            request.setAttribute("error_message", "Enter a password.");
            forward = Path.PAGE_REGISTER;
        }

        LOG.trace("User with id=" + userId + " was saved to the DB");
        LOG.info("User [username --> [" + username + "], [password --> " + password + "] successfully signed up");
        LOG.debug("Register Action finished");

        return forward;
    }

}
