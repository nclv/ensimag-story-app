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
import jakarta.servlet.http.HttpSession;
import models.Story;
import models.User;
import utils.DatabaseManager;
import utils.Path;

public class ShowUserStoriesAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -7614526688665075493L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOG.error("ShowUserStories Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // for POST request on /controller?action=create_story
        // if (user == null) {
        //     request.setAttribute("error_message", "There is no connected user.");
        //     return Path.PAGE_ERROR;
        // }
        LOG.error(user);
        
        StoryDAO storyDAO = new StoryDAOimpl();

        List<Story> stories = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            StoryDAOimpl.setConnection(connection);
            
            stories = storyDAO.findStories(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("stories", stories);
        
        LOG.error("ShowUserStories Action finished");        

        return Path.PAGE_SHOW_USER_STORIES;
    }
    
}
