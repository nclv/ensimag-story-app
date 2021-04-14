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
import dao.RedactionDAO;
import dao.StoryDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Invited;
import models.Paragraphe;
import models.Redaction;
import models.Story;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;
import utils.Validation;

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
            RedactionDAO redactionDAO = daoFactory.getRedactionDAO();

            // get() car existence déjà vérifiée dans les filtres
            Story story = storyDAO.findStory(storyId).get();
            List<Paragraphe> paragraphes = paragrapheDAO.findAllStoryParagraphes(storyId);
            paragraphes.stream().forEach(p -> {
                String content = p.getContent();
                if (content.length() > 15)
                    p.setContent(content.substring(0, 15) + "...");
            });

            // get() car la story existe donc son auteur existe (intégrité BDD)
            User author = userDAO.findUser(story.getUser_id()).get();

            // On récupère mes paragraphes en cours d'écriture
            List<Redaction> myInvalidated = null;
            if (connectedUser != null) {
                myInvalidated = redactionDAO.findAllInvalidatedUser(connectedUser.getId());
                LOG.error(myInvalidated);
            }

            List<Redaction> invalidatedList = redactionDAO.findAllInvalidatedStory(storyId);
            LOG.error(invalidatedList);
            for (Paragraphe paragraphe : paragraphes) {
                for (Redaction redaction : invalidatedList) {
                    if (redaction.getParagraphe_id() == paragraphe.getId()) {
                        paragraphe.setValidated(redaction.isValidated());
                    }
                }
            }
            LOG.error(paragraphes);

            Set<Long> invitedUsersIds = invitedDAO.findAllInvitedUsers(storyId).stream().map(Invited::getUser_id)
                    .collect(Collectors.toSet());
            LOG.error(invitedUsersIds);

            boolean valid = validation(request, connectedUser, author, story, invitedUsersIds);
            if (!valid) {
                return false;
            }

            List<User> invitedUsers = null;
            if (!invitedUsersIds.isEmpty()) {
                invitedUsers = userDAO.findUsers(invitedUsersIds);
            }

            setAttributes(request, story, paragraphes, connectedUser, author, invitedUsersIds, invitedUsers,
                    myInvalidated);

            return true;
        });
        if (result == null) {
            LOG.error("Database query error.");
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }
        LOG.error(result);
        if (!(boolean) result) {
            return Path.PAGE_ERROR;
        }

        LOG.error("ShowStory Action finished");

        return Path.PAGE_SHOW_STORY;
    }

    private boolean validation(HttpServletRequest request, User connectedUser, User author, Story story,
            Set<Long> invitedUsersIds) {
        return Validation.published(request, story) || Validation.author(request, connectedUser, author)
                || (connectedUser != null && invitedUsersIds.contains(connectedUser.getId()));
    }

    private void setAttributes(HttpServletRequest request, Story story, List<Paragraphe> paragraphes,
            User connectedUser, User author, Set<Long> invitedUsersIds, List<User> invitedUsers,
            List<Redaction> myInvalidated) {
        request.setAttribute("story", story);
        request.setAttribute("paragraphes", paragraphes);
        request.setAttribute("author", author);
        request.setAttribute("invitedUsers", invitedUsers);

        if (myInvalidated != null && !myInvalidated.isEmpty()) {
            Redaction redacting = myInvalidated.get(0);
            if (redacting.getStory_id() == story.getId()) {
                request.setAttribute("my_invalidated_paragraphe_id", redacting.getParagraphe_id());
            }
        }

        // On peut lire la story ssi:
        // - il existe au moins un paragraphe final
        // - l'utilisateur connecté n'en est pas l'auteur et la story est publiée
        // - l'utilisateur connecté en est l'auteur
        boolean canRead = paragraphes.stream().map(Paragraphe::isLast).collect(Collectors.toList()).contains(true)
                && ((connectedUser != null && author.getId() == connectedUser.getId()) || story.isPublished());
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
    }
}
