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
import jakarta.servlet.http.HttpSession;
import models.Story;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
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
        LOG.error(user);
        
        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();

            List<Story> stories = storyDAO.findStories(user.getId());

            setAttributes(request, stories);

            return true;
        });
        if (result.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }
        
        LOG.error("ShowUserStories Action finished");        

        return Path.PAGE_SHOW_USER_STORIES;
    }

    private void setAttributes(HttpServletRequest request, List<Story> stories) {
        request.setAttribute("stories", stories);
    }
}
