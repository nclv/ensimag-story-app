package actions;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserDAO;
import dao.UserDAOimpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utils.Path;

public class InviteUsersGetAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -222556178526948159L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOG.error("InviteUsersGet Action starts");
        
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
        long storyId = Long.parseLong(storyIdString);
        LOG.error(storyId);

        UserDAO userDAO = new UserDAOimpl();
        List<User> users = userDAO.findAllUsersExcept(connectedUser.getId());
        request.setAttribute("users", users);

        LOG.error("InviteUsersGet Action finished");

        return Path.PAGE_INVITE_USERS;
    }
    
}
