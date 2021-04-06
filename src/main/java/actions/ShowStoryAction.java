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

public class ShowStoryAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -1044938106365444773L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.error("ShowStory Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");
        LOG.error(connectedUser);

        // Récupération de l'ID de la story
        String storyIdString = request.getParameter("story_id");
        if (storyIdString == null || storyIdString.trim().isEmpty()) {
            LOG.error("Null story_id --> [" + storyIdString + "].");

            request.setAttribute("error_message", "story_id is null.");
            return Path.PAGE_ERROR;
        }
        long storyId;
        try {
            storyId = Long.parseLong(storyIdString);
        } catch (NumberFormatException e) {
            LOG.error("story_id --> [" + storyIdString + "].");

            request.setAttribute("error_message", "story_id is not a number.");
            return Path.PAGE_ERROR;
        }

        StoryDAO storyDAO = new StoryDAOimpl();
        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        UserDAO userDAO = new UserDAOimpl();
        InvitedDAO invitedDAO = new InvitedDAOimpl();

        Story story = null;
        List<Paragraphe> paragraphes = null;
        User author = null;
        Set<Long> invitedUsersIds = null;
        List<User> invitedUsers = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            StoryDAOimpl.setConnection(connection);
            ParagrapheDAOimpl.setConnection(connection);
            UserDAOimpl.setConnection(connection);
            InvitedDAOimpl.setConnection(connection);

            story = storyDAO.findStory(storyId);
            paragraphes = paragrapheDAO.findAllStoryParagraphes(storyId);
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
        request.setAttribute("paragraphes", paragraphes);
        request.setAttribute("author", author);
        request.setAttribute("invitedUsers", invitedUsers);

        // On peut lire la story s'il existe au moins un paragraphe final
        boolean canRead = paragraphes.stream().map(Paragraphe::isLast).collect(Collectors.toList()).contains(true);
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

        LOG.error("ShowStory Action finished");

        return Path.PAGE_SHOW_STORY;
    }

}
