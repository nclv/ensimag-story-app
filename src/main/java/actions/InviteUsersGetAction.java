package actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.StoryDAO;
import dao.StoryDAOimpl;
import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Story;
import models.User;
import utils.DatabaseManager;
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
        if (storyIdString == null || storyIdString.trim().isEmpty()) {
            LOG.error("Null story_id --> [" + storyIdString + "].");

            request.setAttribute("error_message", "story_id is null.");
            return Path.PAGE_ERROR;
        }
        long storyId;
        try {
            storyId = Long.parseLong(storyIdString);
        } catch (NumberFormatException e) {
            LOG.error("story_id --> [" + storyIdString + "].");

            request.setAttribute("error_message", "story_id is not a number.");
            return Path.PAGE_ERROR;
        }
        
        StoryDAO storyDAO = new StoryDAOimpl();
        Story story = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            StoryDAOimpl.setConnection(connection);

            story = storyDAO.findStory(storyId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (story != null && story.isOpen()) {
            LOG.error("Open story.");

            request.setAttribute("error_message", "The story is open. Everyone is invited.");
            return Path.PAGE_ERROR;
        }

        UserDAO userDAO = new UserDAOimpl();
        List<User> users = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            UserDAOimpl.setConnection(connection);
            
            users = userDAO.findAllUsersExcept(connectedUser.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("users", users);

        LOG.error("InviteUsersGet Action finished");

        return Path.PAGE_INVITE_USERS;
    }

}
