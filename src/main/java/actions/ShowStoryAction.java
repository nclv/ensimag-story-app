package actions;

import java.io.IOException;
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

        long storyId = Long.parseLong(request.getParameter("story_id"));
        LOG.error(storyId);

        StoryDAO storyDAO = new StoryDAOimpl();
        Story story = storyDAO.findStory(storyId);
        request.setAttribute("story", story);

        ParagrapheDAO paragrapheDAO = new ParagrapheDAOimpl();
        List<Paragraphe> paragraphes = paragrapheDAO.findAllStoryParagraphes(storyId);
        request.setAttribute("paragraphes", paragraphes);

        UserDAO userDAO = new UserDAOimpl();
        User author = userDAO.findUser(story.getUser_id());
        request.setAttribute("author", author);

        InvitedDAO invitedDAO = new InvitedDAOimpl();
        Set<Long> invitedUsersIds = invitedDAO.findAllInvitedUsers(storyId).stream().map(Invited::getUser_id)
                .collect(Collectors.toSet());
        ;
        LOG.error(invitedUsersIds);
        List<User> invitedUsers = userDAO.findUsers(invitedUsersIds);
        request.setAttribute("invitedUsers", invitedUsers);

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
