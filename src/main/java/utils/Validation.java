package utils;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.ParagrapheDAO;
import dao.RedactionDAO;
import dao.StoryDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Redaction;
import models.Story;
import models.User;

public final class Validation {

    private static final Logger LOG = LogManager.getLogger();

    private static void setErrorMessageAndDispatch(HttpServletRequest req, HttpServletResponse resp, String forwardPage,
            String errorMessage) throws ServletException, IOException {
        req.setAttribute("error_message", errorMessage);
        req.getRequestDispatcher(forwardPage).include(req, resp);
    }

    public static boolean emptyString(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean loggedIn(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        boolean valid = true;
        if (!(session != null && session.getAttribute("user") != null)) {
            LOG.error("You are not logged in.");
            valid = false;

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("not_logged"));
        }
        return valid;
    }

    public static boolean content(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String content = req.getParameter("paragraphe_content");
        List<String> choices = Collections.list(req.getParameterNames()).stream()
                .filter(parameterName -> parameterName.startsWith("choice_")).map(req::getParameter)
                .collect(Collectors.toList());
        boolean valid = true;
        if (emptyString(content)) {
            LOG.error("There is no content --> [" + content + "]");
            valid = false;

            req.setAttribute("choices", choices);
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("empty_content"));
        }
        return valid;
    }

    public static boolean invalidated(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String storyIdString = req.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = req.getParameter("paragraphe_id");
        long paragrapheId = Long.parseLong(paragrapheIdString);

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("connection_error"));
            return false;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.executeAndClose((daoFactory) -> {
            RedactionDAO redactionDAO = daoFactory.getRedactionDAO();

            // On vérifie que l'utilisateur actuel n'édite pas un autre paragraphe. (GET)
            List<Redaction> invalidated = redactionDAO.findAllInvalidated(user.getId());
            LOG.error(invalidated);

            // il n'y a pas de paragraphe non validé
            if (invalidated.isEmpty()) {
                return true;
            }

            // le paragraphe que l'on veut éditer est celui non validé
            Redaction redacting = invalidated.get(0);
            if (redacting.getStory_id() == storyId && redacting.getParagraphe_id() == paragrapheId) {
                return true;
            }
            return false;
        });
        if (result == null) {
            LOG.error("Database query error.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("database_query_error"));
            return false;
        }
        if (!(boolean) result) {
            LOG.error("You are writing another paragraphe ");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("redaction_invalidated"));
            return false;
        }
        return true;
    }

    public static boolean usernamePassword(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // Validation requête
        boolean valid = true;
        if (emptyString(username)) {
            LOG.error("There is no username --> [" + username + "].");
            valid = false;

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("username_empty"));
        } else if (emptyString(password)) {
            LOG.error("There is no password --> [" + username + "]");
            valid = false;

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("password_empty"));
        }
        return valid;
    }

    public static boolean updatePassword(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String new_password = req.getParameter("new_password");
        String new_password_confirmation = req.getParameter("new_password_confirmation");
        // Validation requête
        boolean valid = true;
        if (emptyString(new_password)) {
            LOG.error("There is no password --> [" + new_password + "]");
            valid = false;

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("new_password_empty"));
        } else if (emptyString(new_password_confirmation)) {
            LOG.error("There is no password confirmation --> [" + new_password_confirmation + "]");
            valid = false;

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("new_password_confirmation_empty"));
        } else if (!new_password.trim().equals(new_password_confirmation.trim())) {
            LOG.error("Different passwords --> [" + new_password_confirmation + ", " + new_password + "]");
            valid = false;

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("password_confirmation_error"));
        }
        return valid;
    }

    public static boolean userId(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {

        String userIdString = req.getParameter("user_id");
        if (emptyString(userIdString)) {
            LOG.error("Null user_id--> [" + userIdString + "].");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("userId_empty"));
            return false;
        }
        long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            LOG.error("user_id --> [" + userIdString + "].");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("userId_non_numeric"));
            return false;
        }

        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("connection_error"));
            return false;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        final long userIdFinal = userId;
        Object result = daoManager.executeAndClose((daoFactory) -> {
            UserDAO userDAO = daoFactory.getUserDAO();
            return userDAO.findUser(userIdFinal).isPresent();
        });
        if (result == null) {
            LOG.error("Database query error.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("database_query_error"));
            return false;
        }
        if (!(boolean) result) {
            LOG.error("user_id --> [" + userIdString + "] doesn't exist.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("userId_inexistent"));
            return false;
        }

        return true;
    }

    public static boolean storyId(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String storyIdString = req.getParameter("story_id");

        if (emptyString(storyIdString)) {
            LOG.error("Null story_id --> [" + storyIdString + "].");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("storyId_empty"));
            return false;
        }
        long storyId;
        try {
            storyId = Long.parseLong(storyIdString);
        } catch (NumberFormatException e) {
            LOG.error("story_id --> [" + storyIdString + "].");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("storyId_non_numeric"));
            return false;
        }

        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("connection_error"));
            return false;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        final long storyIdFinal = storyId;
        Object result = daoManager.executeAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();
            return storyDAO.findStory(storyIdFinal).isPresent();
        });
        if (result == null) {
            LOG.error("Database query error.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("database_query_error"));
            return false;
        }
        if (!(boolean) result) {
            LOG.error("story_id --> [" + storyIdString + "] doesn't exist.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("storyId_inexistent"));
            return false;
        }

        return true;
    }

    /**
     * Require a previous call to Validation.storyId
     * 
     * @param req
     * @param resp
     * @param forwardPage
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public static boolean paragrapheId(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {

        String storyIdString = req.getParameter("story_id");
        final long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = req.getParameter("paragraphe_id");

        if (emptyString(paragrapheIdString)) {
            LOG.error("Null paragraphe_id --> [" + paragrapheIdString + "].");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("paragrapheId_empty"));
            return false;
        }
        long paragrapheId;
        try {
            paragrapheId = Long.parseLong(paragrapheIdString);
        } catch (NumberFormatException e) {
            LOG.error("paragraphe_id --> [" + paragrapheIdString + "].");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("paragrapheId_non_numeric"));
            return false;
        }

        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("connection_error"));
            return false;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        final long paragrapheIdFinal = paragrapheId;
        Object result = daoManager.executeAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            return !paragrapheDAO.findParagraphe(storyId, paragrapheIdFinal).isEmpty();
        });
        if (result == null) {
            LOG.error("Database query error.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("database_query_error"));
            return false;
        }
        if (!(boolean) result) {
            LOG.error("story_id, paragraphe_id --> [" + storyId + ", " + paragrapheIdString + "] doesn't exist.");

            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("paragrapheId_inexistent"));
            return false;
        }

        return true;
    }

    public static boolean published(HttpServletRequest request, User connectedUser, User author, Story story) {
        boolean valid = true;
        // Si la story n'est pas publiée et que je n'en suis pas l'auteur.
        // ou la story n'est pas publiée et aucun utilisateur n'est connecté
        if (!story.isPublished()
                && ((connectedUser != null && author.getId() != connectedUser.getId()) || connectedUser == null)) {
            LOG.error("The story is not published and you are not it's author: " + story + ", " + connectedUser + ", "
                    + author);

            request.setAttribute("error_message", ErrorMessage.get("story_not_published"));
            valid = false;
        }
        return valid;
    }

    public static boolean finalChoice(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        boolean isFinal = req.getParameter("is_final").equals("final") ? true : false;
        List<String> choices = Collections.list(req.getParameterNames()).stream()
                .filter(parameterName -> parameterName.startsWith("choice_")).map(req::getParameter)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toList());

        LOG.error(choices);
        LOG.error(choices.isEmpty());
        LOG.error(isFinal);
        LOG.error(choices.isEmpty() && !isFinal);
        boolean valid = true;
        if (choices.isEmpty() && !isFinal) {
            LOG.error("You can't create a paragraphe non final without a choice.");
            valid = false;

            req.setAttribute("choices", choices);
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("no_final_no_choices"));
        }
        return valid;
    }

    public static boolean createAndPublishFinal(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        boolean isFinal = req.getParameter("is_final").equals("final") ? true : false;
        List<String> choices = Collections.list(req.getParameterNames()).stream()
                    .filter(parameterName -> parameterName.startsWith("choice_")).map(req::getParameter)
                    .collect(Collectors.toList());

        boolean valid = true;
        if (req.getParameter("create_and_publish") != null && !isFinal) {
            LOG.error("Your can't published a story without any final paragraphe.");
            valid = false;

            req.setAttribute("choices", choices);
            setErrorMessageAndDispatch(req, resp, forwardPage, ErrorMessage.get("create_publish_no_final"));
        }
        return valid;
    }
}
