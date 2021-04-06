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

public class UpdatePasswordAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = 1208690801319485534L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("UpdatePassword Action starts");

        String new_password = request.getParameter("new_password");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String password = BCrypt.hashpw(new_password, BCrypt.gensalt());
        user.setPassword(password);

        UserDAO userDao = new UserDAOimpl();

        boolean err = false;
        try (Connection connection = DatabaseManager.getConnection()) {
            UserDAOimpl.setConnection(connection);
            
            userDao.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            err = true;
        }
        
        if (err) {
            request.setAttribute("error_message", "Database error. Please retry.");
            return Path.PAGE_UPDATE_PASSWORD;
        }

        LOG.debug("UpdatePassword Action finished");

        return Path.REDIRECT_LOGIN;
    }
    
}
