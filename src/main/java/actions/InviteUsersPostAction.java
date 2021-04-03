package actions;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.InvitedDAO;
import dao.InvitedDAOimpl;
import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Invited;
import models.User;
import utils.Path;

public class InviteUsersPostAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -222556178526948159L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.error("InviteUsersPost Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");
        if (connectedUser == null) {
            request.setAttribute("error_message", "There is no connected user.");
            return Path.PAGE_ERROR;
        }

        String storyIdString = request.getParameter("story_id");
        if (storyIdString == null) {
            LOG.error("Null story_id --> [" + storyIdString + "].");

            request.setAttribute("error_message", "story_id is null.");
            return Path.PAGE_ERROR;
        }
        Long storyId = Long.parseLong(storyIdString);
        long[] invitedUserIds = Arrays.stream(request.getParameterValues("selected")).mapToLong(Long::valueOf).toArray();
        
        Invited invited = Invited.builder().date(new Date(System.currentTimeMillis())).story_id(storyId).build();
        InvitedDAO invitedDAO = new InvitedDAOimpl();

        long err;
        for (long invitedUserId : invitedUserIds) {
            invited.setUser_id(invitedUserId);
            err = invitedDAO.saveInvited(invited);
            if (err == -1) {
                UserDAO userDAO = new UserDAOimpl();
                User user = userDAO.findUser(invitedUserId);

                LOG.error("Can't insert --> [" + user + "]. Already invited.");

                // request.setAttribute("error_message", user.getName() + " is already invited.");
                return Path.REDIRECT_INVITE_USERS + "&story_id=" + storyIdString + "&error_message=" + user.getName() + " is already invited.";
            }
        }

        LOG.error("InviteUsersPost Action finished");

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }

}
