package actions;

import java.io.IOException;

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
import utils.Path;

public class UpdatePasswordAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = 1208690801319485534L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("UpdatePassword Action starts");

        String new_password = request.getParameter("new_password");
        String new_password_confirmation = request.getParameter("new_password_confirmation");
        // Validation requête
        if (new_password == null || new_password.trim().isEmpty()) {
            LOG.error("There is no password --> [" + new_password + "]");

            request.setAttribute("error_message", "Enter a new password.");
            return Path.PAGE_UPDATE_PASSWORD;
        } else if (new_password_confirmation == null || new_password_confirmation.trim().isEmpty()) {
            LOG.error("There is no password confirmation --> [" + new_password_confirmation + "]");

            request.setAttribute("error_message", "Enter a new password confirmation.");
            return Path.PAGE_UPDATE_PASSWORD;
        } else if (!new_password.trim().equals(new_password_confirmation.trim())) {
            LOG.error("Different passwords --> [" + new_password_confirmation + ", " + new_password + "]");

            request.setAttribute("error_message", "Enter the same password twice.");
            return Path.PAGE_UPDATE_PASSWORD;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error_message", "There is no connected user.");
            return Path.PAGE_ERROR;
        }

        String password = BCrypt.hashpw(new_password, BCrypt.gensalt());
        user.setPassword(password);

        UserDAO userDao = new UserDAOimpl();
        int err = userDao.updateUser(user);
        if (err == -1) {
            request.setAttribute("error_message", "Database error. Please retry.");
            return Path.PAGE_UPDATE_PASSWORD;
        }

        LOG.debug("UpdatePassword Action finished");

        return Path.REDIRECT_LOGIN;
    }
    
}
