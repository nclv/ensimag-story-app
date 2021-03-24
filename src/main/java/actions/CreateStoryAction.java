package actions;

import java.io.IOException;

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
import utils.Path;

public class CreateStoryAction implements Action {
    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -1130235348120525920L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("CreateStory Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // for POST request on /controller?action=create_story
        if (user == null) {
            request.setAttribute("error_message", "There is no connected user.");
            return Path.PAGE_ERROR;
        }

        Boolean open = (request.getParameter("open") == "open")? true: false;

        Story story = null;
        if (request.getParameter("create") != null) {
            // create action
            story = Story.builder().open(open).user_id(user.getId()).published(false).build();
    
        } else if (request.getParameter("create_and_publish") != null) {
            // create and publish action
            story = Story.builder().open(open).user_id(user.getId()).published(true).build();
        }

        StoryDAO storyDAO = new StoryDAOimpl();
        long storyId = storyDAO.saveStory(story);
        LOG.error(storyId + " " + story);

        // Validation
        String forward = Path.PAGE_SHOW_STORIES;
        // TODO

        LOG.debug("CreateStory Action finished");
        return forward;
    }

}
