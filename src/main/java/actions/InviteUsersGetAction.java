package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.StoryDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Story;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class InviteUsersGetAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -222556178526948159L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.error("InviteUsersGet Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");

        // Récupération de l'ID de la story
        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.executeAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();
            UserDAO userDAO = daoFactory.getUserDAO();

            Optional<Story> story = storyDAO.findStory(storyId);
            List<User> users = userDAO.findAllUsersExcept(connectedUser.getId());

            boolean valid = validation(request, story);

            if (valid) {
                setAttributes(request, users);
            }

            return valid;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }
        if (!(boolean) result) {
            return Path.PAGE_ERROR;
        }

        LOG.error("InviteUsersGet Action finished");

        return Path.PAGE_INVITE_USERS;
    }

    private void setAttributes(HttpServletRequest request, List<User> users) {
        request.setAttribute("users", users);
    }

    private boolean validation(HttpServletRequest request, Optional<Story> story) {
        boolean valid = true;
        if (story.isPresent() && story.get().isOpen()) {
            LOG.error("Open story.");

            request.setAttribute("error_message", ErrorMessage.get("open_story"));
            valid = false;
        }

        return valid;
    }
}
