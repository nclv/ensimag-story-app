package actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.InvitedDAO;
import dao.InvitedDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Invited;
import utils.DatabaseManager;
import utils.Path;

public class RemoveInvitedAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = -2448184992207676555L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String userIdString = request.getParameter("user_id");
        if (userIdString == null || userIdString.trim().isEmpty()) {
            LOG.error("Null user_id--> [" + userIdString + "].");

            request.setAttribute("error_message", "user_id is null.");
            return Path.PAGE_ERROR;
        }
        long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            LOG.error("user_id --> [" + userIdString + "].");

            request.setAttribute("error_message", "user_id is/are not a number.");
            return Path.PAGE_ERROR;
        }

        Invited invited = Invited.builder().user_id(userId).story_id(storyId).build();

        InvitedDAO invitedDAO = new InvitedDAOimpl();

        boolean err = false;
        try (Connection connection = DatabaseManager.getConnection()) {
            InvitedDAOimpl.setConnection(connection);

            invitedDAO.removeInvited(invited);
        } catch (SQLException e) {
            e.printStackTrace();
            err = true;
        }

        if (err) {
            LOG.error("Couldn't remove invited user. " + invited);

            request.setAttribute("error_message", "Couldn't remove invited user.");
            return Path.PAGE_ERROR;
        }

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }
}
