package actions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ParagrapheDAO;
import dao.ParagrapheDAOimpl;
import dao.StoryDAO;
import dao.StoryDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Paragraphe;
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

        boolean open = request.getParameter("open").equals("open") ? true : false;
        boolean is_final = request.getParameter("is_final").equals("final") ? true : false;
        LOG.error("Open story: " + open);
        LOG.error("Final paragraphe: " + is_final);

        Story story = null;
        if (request.getParameter("create") != null) {
            // create action
            story = Story.builder().open(open).user_id(user.getId()).published(false).build();

        } else if (request.getParameter("create_and_publish") != null) {
            // create and publish action
            story = Story.builder().open(open).user_id(user.getId()).published(true).build();
        }

        String content = request.getParameter("first_paragraphe_content");
        if (content == null || content.trim().isEmpty()) {
            LOG.error("There is no content --> [" + content + "]");

            request.setAttribute("error_message", "Enter a first paragraphe.");
            return Path.PAGE_CREATE_STORY;
        }

        // Database
        StoryDAO storyDAO = new StoryDAOimpl();
        long storyId = storyDAO.saveStory(story);
        LOG.error(storyId + " " + story);

        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).content(content).is_final(is_final).build();

        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        long paragrapheId = paragrapheDAO.saveParagraphe(paragraphe);
        LOG.error(paragrapheId + " " + paragraphe);

        // Validation story and paragraphe database
        String forward = Path.REDIRECT_SHOW_USER_STORIES;
        if (storyId == -1 && paragrapheId == -1) {
            request.setAttribute("error_message",
                    "Error when creating your story. Fill the fields and submit your story again.");
            forward = Path.PAGE_CREATE_STORY;
        }

        LOG.debug("CreateStory Action finished");
        return forward;
    }

}
