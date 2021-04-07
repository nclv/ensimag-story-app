/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.InvitedDAO;
import dao.ParagrapheDAO;
import dao.StoryDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Invited;
import models.Paragraphe;
import models.Story;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
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

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_CREATE_STORY;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Optional<Object> result = daoManager.transactionAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            UserDAO userDAO = daoFactory.getUserDAO();
            InvitedDAO invitedDAO = daoFactory.getInvitedDAO();

            // get() car existence déjà vérifiée dans les filtres
            Story story = storyDAO.findStory(storyId).get();
            Paragraphe paragraphe = paragrapheDAO.findParagraphe(storyId, paragrapheId).get();

            // get() car la story existe donc son auteur existe (intégrité BDD)
            User author = userDAO.findUser(story.getUser_id()).get();

            Set<Long> invitedUsersIds = invitedDAO.findAllInvitedUsers(storyId).stream().map(Invited::getUser_id)
                    .collect(Collectors.toSet());
            LOG.error(invitedUsersIds);

            List<User> invitedUsers = null;
            if (!invitedUsersIds.isEmpty()) {
                invitedUsers = userDAO.findUsers(invitedUsersIds);
            }

            request.setAttribute("story", story);
            request.setAttribute("paragraphe", paragraphe);
            request.setAttribute("author", author);
            request.setAttribute("invitedUsers", invitedUsers);

            // On peut lire la story s'il existe au moins un paragraphe final
            boolean canRead = paragraphe.isLast();
            request.setAttribute("canRead", canRead);

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

            return true;
        });
        if (result.isEmpty()) {
            LOG.error("Database query error.");
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }

        LOG.error("ShowParagraphe Action finished");

        return Path.PAGE_SHOW_PARAGRAPHE;
    }

}
