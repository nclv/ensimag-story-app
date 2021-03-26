package actions;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.StoryDAO;
import dao.StoryDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Story;
import utils.Path;

public class HomePageAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 4716393950785756370L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOG.error("HomePage Action starts");
        
        StoryDAO storyDAO = new StoryDAOimpl();
        List<Story> stories = storyDAO.findAllOpenPublishedStories();
        request.setAttribute("stories", stories);
        
        LOG.error("HomePage Action finished");        

        return Path.PAGE_HOME;
    }
    
}
