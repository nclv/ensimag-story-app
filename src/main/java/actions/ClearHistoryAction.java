package actions;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Historic;
import models.User;
import utils.Path;

public class ClearHistoryAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = -292225470317908533L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.error("ClearHistory Action starts");

        HttpSession session = request.getSession();

        // Récupération de l'ID de la story
        String storyIdString = request.getParameter("story_id");

        session.setAttribute("history", new LinkedList<Historic>());

        LOG.error("ClearHistory Action finished");

        return Path.REDIRECT_READ_STORY + "&story_id=" + storyIdString;
    }

}
