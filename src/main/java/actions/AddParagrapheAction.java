/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author vincent
 */
public class AddParagrapheAction implements Action{
    
    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 254668985512218466L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("AddParagraphe Action starts");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            request.setAttribute("error_message", "There is no connected user.");
            return Path.PAGE_ERROR;
        }
        
        boolean is_final = request.getParameter("is_final").equals("final") ? true : false;
        
        String content = request.getParameter("paragraphe_content");
        
        if (content == null || content.trim().isEmpty()) {
            LOG.error("There is no content --> [" + content + "]");

            request.setAttribute("error_message", "Enter a paragraphe.");
            return Path.PAGE_ADD_PARAGRAPHE;
        }
        long storyId = 4;
        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).content(content).is_final(is_final).build();
        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        long paragrapheId = paragrapheDAO.saveParagraphe(paragraphe);
        LOG.error(paragrapheId + " " + paragraphe);
        
        String forward = Path.REDIRECT_SHOW_USER_STORIES;
        if (paragrapheId == -1) {
            request.setAttribute("error_message",
                    "Error when adding your paragraphe. Fill the fields and submit your paragraphe again.");
            forward = Path.PAGE_ADD_PARAGRAPHE;
        }
        
        LOG.debug("AddParagraphe Action finished");
        return forward;
    }
}
