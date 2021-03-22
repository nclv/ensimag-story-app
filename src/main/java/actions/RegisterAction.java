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

        LOG.trace("User with id=" + userId + " was saved to the DB");
        LOG.info("User [username --> [" + username + "], [password --> " + password + "] successfully signed up");
        LOG.debug("Register Action finished");

        return Path.REDIRECT_LOGIN;
    }

}
