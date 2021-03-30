package actions;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ParagrapheDAO;
import dao.ParagrapheDAOimpl;
import dao.StoryDAO;
import dao.StoryDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Paragraphe;
import models.Story;
import utils.Path;

public class ShowStoryAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -1044938106365444773L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOG.error("ShowStory Action starts");

        long storyId = Long.parseLong(request.getParameter("story_id"));
        LOG.error(storyId);
        
        StoryDAO storyDAO = new StoryDAOimpl();
        Story story = storyDAO.findStory(storyId);
        request.setAttribute("story", story);

        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        List<Paragraphe> paragraphes = paragrapheDAO.findAllStoryParagraphes(storyId);
        request.setAttribute("paragraphes", paragraphes);
        
        LOG.error("ShowStory Action finished");        

        return Path.PAGE_SHOW_STORY;
    }
    
}
