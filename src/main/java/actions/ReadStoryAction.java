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
import utils.Validation;

public class ReadStoryAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = 5509333508849940453L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.error("ReadStory Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");
        LOG.error(connectedUser);

        // Récupération de l'ID de la story
        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.transactionAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            UserDAO userDAO = daoFactory.getUserDAO();
            InvitedDAO invitedDAO = daoFactory.getInvitedDAO();

            // get() car existence déjà vérifiée dans les filtres
            Story story = storyDAO.findStory(storyId).get();
            List<Paragraphe> paragraphes = paragrapheDAO.findAllStoryParagraphes(storyId);

            // get() car la story existe donc son auteur existe (intégrité BDD)
            User author = userDAO.findUser(story.getUser_id()).get();

            boolean valid = validation(request, connectedUser, author, story);
            if (!valid) {
                return false;
            }

            Set<Long> invitedUsersIds = invitedDAO.findAllInvitedUsers(storyId).stream().map(Invited::getUser_id)
                    .collect(Collectors.toSet());
            Set<Long> redactorsIds = paragrapheDAO.findAllStoryParagraphes(storyId).stream().map(Paragraphe::getUser_id)
                    .collect(Collectors.toSet());
            LOG.error(redactorsIds);

            List<User> redactors = null;
            if (!redactorsIds.isEmpty()) {
                redactors = userDAO.findUsers(redactorsIds);
            }

            setAttributes(request, story, paragraphes, connectedUser, author, invitedUsersIds, redactors);

            return true;
        });
        if (result == null) {
            LOG.error("Database query error.");
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }
        if (!(boolean) result) {
            return Path.PAGE_ERROR;
        }

        LOG.error("ReadStory Action finished");

        return Path.PAGE_READ_STORY;
    }

    private boolean validation(HttpServletRequest request, User connectedUser, User author, Story story) {
        return Validation.published(request, connectedUser, author, story);
    }

    private void setAttributes(HttpServletRequest request, Story story, List<Paragraphe> paragraphes,
            User connectedUser, User author, Set<Long> invitedUsersIds, List<User> redactors) {
        request.setAttribute("story", story);
        request.setAttribute("paragraphes", paragraphes);
        request.setAttribute("author", author);
        request.setAttribute("redactors", redactors);

        // On peut éditer la story ssi. un utilisateur est connecté et
        // - elle est ouverte,
        // - elle est fermée et l'utilisateur connecté est invité,
        // - elle est fermée et l'utilisateur connecté en est l'auteur
        if (connectedUser != null && (story.isOpen() || (!story.isOpen()
                && (invitedUsersIds.contains(connectedUser.getId()) || author.getId() == connectedUser.getId())))) {
            LOG.error("canEditStory");
            request.setAttribute("canEditStory", true);
        }
    }
}
