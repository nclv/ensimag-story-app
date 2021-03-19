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

    private static final long serialVersionUID = 1L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("Command starts");

        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDao = new UserDAOimpl();
        User user = userDao.findUser(username);

        String forward = Path.REDIRECT_HOME;

        request.setAttribute("usernameNotFound", username);
        if (user == null) {
            LOG.error("There is no such a username --> [" + username + "]");

            request.setAttribute("usernameValid", "is-invalid");
            forward = Path.PAGE_LOGIN;
        } else if (!password.equals(user.getPassword())) {
            LOG.error("Incorrect password --> " + password + " for [" + username + "]");

            request.setAttribute("passwordValid", "is-invalid");
            forward = Path.PAGE_LOGIN;
        } else {
            LOG.trace("User [username --> '" + username + "', password --> '" + password + "'] successfully signed in");

            session.setAttribute("user", user);
        }

        LOG.debug("Command finished");

        return forward;
    }

}