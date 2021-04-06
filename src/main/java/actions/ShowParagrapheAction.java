/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import utils.DatabaseManager;
import utils.Path;

/**
 *
 * @author vincent
 */

public class ShowParagrapheAction implements Action {

    private static final long serialVersionUID = -1681360924475966816L;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.error("ShowParagraphe Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");
        LOG.error(connectedUser);

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = request.getParameter("paragraphe_id");
        long paragrapheId = Long.parseLong(paragrapheIdString);
        
        StoryDAO storyDAO = new StoryDAOimpl();
        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        UserDAO userDAO = new UserDAOimpl();
        InvitedDAO invitedDAO = new InvitedDAOimpl();

        Story story = null;
        Paragraphe paragraphe = null;
        User author = null;
        Set<Long> invitedUsersIds = null;
        List<User> invitedUsers = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            StoryDAOimpl.setConnection(connection);
            ParagrapheDAOimpl.setConnection(connection);
            UserDAOimpl.setConnection(connection);
            InvitedDAOimpl.setConnection(connection);

            story = storyDAO.findStory(storyId);
            paragraphe = paragrapheDAO.findParagraphe(storyId, paragrapheId);
            author = userDAO.findUser(story.getUser_id());

            invitedUsersIds = invitedDAO.findAllInvitedUsers(storyId).stream().map(Invited::getUser_id)
                    .collect(Collectors.toSet());
            LOG.error(invitedUsersIds);
            if (!invitedUsersIds.isEmpty()) {
                invitedUsers = userDAO.findUsers(invitedUsersIds);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("story", story);
        request.setAttribute("paragraphe", paragraphe);
        request.setAttribute("author", author);
        request.setAttribute("invitedUsers", invitedUsers);

        // On peut lire la story s'il existe au moins un paragraphe final
        if (paragraphe != null) {
            boolean canRead = paragraphe.isLast();
            request.setAttribute("canRead", canRead);
        }

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
