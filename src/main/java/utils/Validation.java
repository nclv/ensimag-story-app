package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ParagrapheDAO;
import dao.ParagrapheDAOimpl;
import dao.StoryDAO;
import dao.StoryDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Story;

public class Validation {

    private static final Logger LOG = LogManager.getLogger();

    public static boolean usernamePassword(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // Validation requête
        boolean valid = true;
        if (username == null || username.trim().isEmpty()) {
            LOG.error("There is no username --> [" + username + "]");
            valid = false;

            req.setAttribute("error_message", "Enter an user name.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        } else if (password == null || password.trim().isEmpty()) {
            LOG.error("There is no password --> [" + username + "]");
            valid = false;

            req.setAttribute("error_message", "Enter a password.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }
        return valid;
    }

    public static boolean updatePassword(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String new_password = req.getParameter("new_password");
        String new_password_confirmation = req.getParameter("new_password_confirmation");
        // Validation requête
        boolean valid = true;
        if (new_password == null || new_password.trim().isEmpty()) {
            LOG.error("There is no password --> [" + new_password + "]");
            valid = false;

            req.setAttribute("error_message", "Enter a new password.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        } else if (new_password_confirmation == null || new_password_confirmation.trim().isEmpty()) {
            LOG.error("There is no password confirmation --> [" + new_password_confirmation + "]");
            valid = false;

            req.setAttribute("error_message", "Enter a new password confirmation.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        } else if (!new_password.trim().equals(new_password_confirmation.trim())) {
            LOG.error("Different passwords --> [" + new_password_confirmation + ", " + new_password + "]");
            valid = false;

            req.setAttribute("error_message", "Enter the same password twice.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }
        return valid;
    }

    public static boolean storyId(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String storyIdString = req.getParameter("story_id");

        boolean valid = true;
        if (storyIdString == null || storyIdString.trim().isEmpty()) {
            LOG.error("Null story_id --> [" + storyIdString + "].");
            valid = false;

            req.setAttribute("error_message", "story_id is null.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }
        long storyId = -1;
        try {
            storyId = Long.parseLong(storyIdString);
        } catch (NumberFormatException e) {
            LOG.error("story_id --> [" + storyIdString + "].");
            valid = false;

            req.setAttribute("error_message", "story_id is not a number.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }

        StoryDAO storyDAO = new StoryDAOimpl();
        try (Connection connection = DatabaseManager.getConnection()) {
            StoryDAOimpl.setConnection(connection);

            if (storyDAO.findStory(storyId) == null) {
                LOG.error("story_id --> [" + storyIdString + "] doesn't exist.");
                valid = false;

                req.setAttribute("error_message", "story_id doesn't exist.");
                req.getRequestDispatcher(forwardPage).include(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid;
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
        long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = req.getParameter("paragraphe_id");

        boolean valid = true;
        if (paragrapheIdString == null || paragrapheIdString.trim().isEmpty()) {
            LOG.error("Null paragraphe_id --> [" + paragrapheIdString + "].");
            valid = false;

            req.setAttribute("error_message", "paragraphe_id is null.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }
        long paragrapheId = -1;
        try {
            paragrapheId = Long.parseLong(paragrapheIdString);
        } catch (NumberFormatException e) {
            LOG.error("paragraphe_id --> [" + paragrapheIdString + "].");
            valid = false;

            req.setAttribute("error_message", "paragraphe_id is not a number.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }

        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        try (Connection connection = DatabaseManager.getConnection()) {
            ParagrapheDAOimpl.setConnection(connection);

            if (paragrapheDAO.findParagraphe(storyId, paragrapheId) == null) {
                LOG.error("story_id, paragraphe_id --> [" + storyId + ", " + paragrapheIdString + "] doesn't exist.");
                valid = false;

                req.setAttribute("error_message", "story_id, paragraphe_id doesn't exist.");
                req.getRequestDispatcher(forwardPage).include(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid;
    }
}
