package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.StoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Story;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class PublishUnpublishStoryAction implements Action {

    private static final long serialVersionUID = 4186827675025942441L;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("PublishUnpublishStory Action start");

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

            // on peut get()
            Story story = storyDAO.findStory(storyId).get();
            story.setPublished(!story.isPublished());
            storyDAO.updateStory(story);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_publish_story_error"));
            return Path.PAGE_ERROR;
        }

        LOG.debug("PublishUnpublishStory Action finished");

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }

}
