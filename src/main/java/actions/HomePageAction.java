package actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.StoryDAO;
import dao.StoryDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Story;
import utils.DatabaseManager;
import utils.Path;

public class HomePageAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 4716393950785756370L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOG.error("HomePage Action starts");
        
        StoryDAO storyDAO = new StoryDAOimpl();

        List<Story> stories = null;
        List<Story> published_stories = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            StoryDAOimpl.setConnection(connection);

            stories = storyDAO.findAllOpenPublishedStories();
            published_stories = storyDAO.findAllPublishedStories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("stories", stories);
        request.setAttribute("published_stories", published_stories);
        
        LOG.error("HomePage Action finished");        

        return Path.PAGE_HOME;
    }
    
}
