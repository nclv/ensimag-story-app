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

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.executeAndClose((daoFactory) -> {
            UserDAO userDAO = daoFactory.getUserDAO();

            userDAO.updateUser(user);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_update_password_error"));
            return Path.PAGE_UPDATE_PASSWORD;
        }

        LOG.debug("UpdatePassword Action finished");

        return Path.REDIRECT_LOGIN;
    }
    
}
