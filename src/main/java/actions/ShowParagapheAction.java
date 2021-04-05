/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.InvitedDAO;
import dao.InvitedDAOimpl;
import dao.ParagrapheDAO;
import dao.ParagrapheDAOimpl;
import dao.StoryDAO;
import dao.StoryDAOimpl;
import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Invited;
import models.Paragraphe;
import models.Story;
import models.User;
import utils.Path;

/**
 *
 * @author vincent
 */


public class ShowParagapheAction implements Action {
    
    private static final Logger LOG = LogManager.getLogger();
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.error("ShowParagraphe Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");
        LOG.error(connectedUser);

        // Récupération de l'ID du paragraphe
        String paragrapheIdString = request.getParameter("paragraphe_id");
        if (paragrapheIdString == null || paragrapheIdString.trim().isEmpty()) {
            LOG.error("Null story_id --> [" + paragrapheIdString + "].");

            request.setAttribute("error_message", "story_id is null.");
            return Path.PAGE_ERROR;
        }
        long paragrapheId;
        try {
            paragrapheId = Long.parseLong(paragrapheIdString);
        } catch (NumberFormatException e) {
            LOG.error("paragraphe_id --> [" + paragrapheIdString + "].");

            request.setAttribute("error_message", "paragraphe_id is not a number.");
            return Path.PAGE_ERROR;
        }
        
        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        StoryDAO storyDAO = new StoryDAOimpl();
        Story story = storyDAO.findStory(storyId);
        request.setAttribute("story", story);

        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        Paragraphe paragraphe = paragrapheDAO.findParagraphe(storyId, paragrapheId);
        request.setAttribute("paragraphe", paragraphe);
        // On peut lire la story s'il existe au moins un paragraphe final
        boolean canRead = paragraphe.isLast();
        request.setAttribute("canRead", canRead);

        UserDAO userDAO = new UserDAOimpl();
        User author = userDAO.findUser(story.getUser_id());
        request.setAttribute("author", author);

        InvitedDAO invitedDAO = new InvitedDAOimpl();
        Set<Long> invitedUsersIds = invitedDAO.findAllInvitedUsers(storyId).stream().map(Invited::getUser_id)
                .collect(Collectors.toSet());
        ;
        LOG.error(invitedUsersIds);
        List<User> invitedUsers = userDAO.findUsers(invitedUsersIds);
        request.setAttribute("invitedUsers", invitedUsers);

        // On peut éditer la story ssi. un utilisateur est connecté et
        // - elle est ouverte,
        // - elle est fermée et l'utilisateur connecté est invité,
        // - elle est fermée et l'utilisateur connecté en est l'auteur
        if (connectedUser != null && (story.isOpen() || (!story.isOpen()
                && (invitedUsersIds.contains(connectedUser.getId()) || author.getId() == connectedUser.getId())))) {
            LOG.error("canEditStory");
            request.setAttribute("canEditStory", true);
        }

        if (!story.isOpen() && connectedUser != null && author.getId() == connectedUser.getId()) {
            LOG.error("canInvite");
            request.setAttribute("canInvite", true);
        }

        LOG.error("ShowParagraphe Action finished");

        return Path.PAGE_SHOW_PARAGRAPHE;
    }
    
}
