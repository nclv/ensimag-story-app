package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.InvitedDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Invited;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class RemoveInvitedAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = -2448184992207676555L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("RemoveInvited Action start");

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String userIdString = request.getParameter("user_id");
        long userId = Long.parseLong(userIdString);

        Invited invited = Invited.builder().user_id(userId).story_id(storyId).build();

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            InvitedDAO invitedDAO = daoFactory.getInvitedDAO();

            invitedDAO.removeInvited(invited);

            return true;
        });
        if (result.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("database_remove_invited_user_error"));
            return Path.PAGE_ERROR;
        }

        LOG.debug("RemoveInvited Action finished");

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }
}
