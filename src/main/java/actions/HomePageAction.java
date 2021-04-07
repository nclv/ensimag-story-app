package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
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

public class HomePageAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 4716393950785756370L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.error("HomePage Action starts");

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();

            List<Story> stories = storyDAO.findAllOpenPublishedStories();
            List<Story> publishedStories = storyDAO.findAllPublishedStories();

            request.setAttribute("stories", stories);
            request.setAttribute("published_stories", publishedStories);
            // var object = new Object() {
            // public final List<Story> stories = storyDAO.findAllOpenPublishedStories();
            // public final List<Story> publishedStories =
            // storyDAO.findAllPublishedStories();
            // };
            // LOG.error(object.stories);
            // LOG.error(object.publishedStories);
            // return object;
            return true;
        });
        if (result.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }

        // Object attributes = result.get();
        // SYMBOL NOT FOUND
        // request.setAttribute("stories", attributes.stories);
        // request.setAttribute("published_stories", attributes.publishedStories);

        LOG.error("HomePage Action finished");

        return Path.PAGE_HOME;
    }

}
