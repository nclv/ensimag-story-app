package actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

public class InviteUsersPostAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -222556178526948159L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.error("InviteUsersPost Action starts");

        // Récupération de l'ID de la story
        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        // Récupération des utilisateurs sélectionnés
        Set<Long> invitedUserIds = Arrays.stream(request.getParameterValues("selected")).mapToLong(Long::valueOf)
                .boxed().collect(Collectors.toSet());

        Invited invited = Invited.builder().date(new Date(System.currentTimeMillis())).story_id(storyId).build();
        InvitedDAO invitedDAO = new InvitedDAOimpl();

        boolean err = false;
        try (Connection connection = DatabaseManager.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            InvitedDAOimpl.setConnection(connection);
            try {
                // Récupération des utilisateurs déja invités
                Set<Long> alreadyInvitedUserIds = invitedDAO.findAllInvitedUsers(storyId).stream()
                        .map(Invited::getUser_id).collect(Collectors.toSet());

                // On ne garde que les utilisateurs qui ne sont pas déjà invités.
                Set<Long> nonInvitedUserIds = new HashSet<>(invitedUserIds);
                nonInvitedUserIds.removeAll(alreadyInvitedUserIds);

                for (long invitedUserId : nonInvitedUserIds) {
                    invited.setUser_id(invitedUserId);
                    invitedDAO.saveInvited(invited);
                }

                connection.commit();
                connection.setAutoCommit(autoCommit);
            } catch (SQLException rollbackError) {
                rollbackError.printStackTrace();
                err = true;
                try {
                    connection.rollback();
                } catch (SQLException rbe) {
                    rollbackError.printStackTrace();
                }
            }
        } catch (SQLException connectionError) {
            connectionError.printStackTrace();
            err = true;
        }

        if (err) {
            request.setAttribute("error_message", "User is already invited. (database error)");
            // return Path.REDIRECT_INVITE_USERS + "&story_id=" + storyIdString +
            // "&error_message=" + user.getName()
            // + " is already invited.";
            return Path.PAGE_ERROR;
        }

        LOG.error("InviteUsersPost Action finished");

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }

}
