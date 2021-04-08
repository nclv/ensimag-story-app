package actions;

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
import dao.ParentSectionDAO;
import dao.StoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Paragraphe;
import models.ParentSection;
import models.Story;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class CreateStoryAction implements Action {
    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -1130235348120525920L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("CreateStory Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        boolean open = request.getParameter("open").equals("open") ? true : false;
        boolean is_final = request.getParameter("is_final").equals("final") ? true : false;
        LOG.error("Open story: " + open);
        LOG.error("Final paragraphe: " + is_final);

        List<String> choices = Collections.list(request.getParameterNames()).stream()
                .filter(parameterName -> parameterName.startsWith("choice_")).map(request::getParameter)
                .collect(Collectors.toList());
        LOG.error(choices);

        Story story = null;
        if (request.getParameter("create") != null) {
            // create action
            story = Story.builder().open(open).user_id(user.getId()).published(false).build();

        } else if (request.getParameter("create_and_publish") != null) {
            // create and publish action
            story = Story.builder().open(open).user_id(user.getId()).published(true).build();
        }

        String content = request.getParameter("first_paragraphe_content");

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            request.setAttribute("choices", choices);
            return Path.PAGE_CREATE_STORY;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        final Story storyFinal = story;
        Object result = daoManager.transactionAndClose((daoFactory) -> {
            StoryDAO storyDAO = daoFactory.getStoryDAO();
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            ParentSectionDAO parentSectionDAO = daoFactory.getParentSectionDAO();

            long storyId = storyDAO.saveStory(storyFinal);
            LOG.error(storyId + " " + storyFinal);

            Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).user_id(user.getId()).content(content)
                    .last(is_final).build();

            long parentParagrapheId = paragrapheDAO.saveParagraphe(paragraphe);
            LOG.error(parentParagrapheId + " " + paragraphe);

            ParentSection parentSection = ParentSection.builder().story_id(storyId).parent_story_id(storyId)
                    .parent_paragraphe_id(parentParagrapheId).paragraphe_conditionnel_story_id(storyId)
                    .paragraphe_conditionnel_id(parentParagrapheId).build();
            long paragrapheId;
            paragraphe = Paragraphe.builder().story_id(storyId).user_id(user.getId()).content("NO CONTENT YET").build();

            int index = 0;
            for (String choice : choices) {
                paragrapheId = paragrapheDAO.saveParagraphe(paragraphe);

                parentSection.setChoice_text(choice);
                parentSection.setChoice_number(index);
                parentSection.setParagraphe_id(paragrapheId);

                parentSectionDAO.saveParentSection(parentSection);

                index++;
            }
            return true;
        });
        if (result == null) {
            LOG.error("Database query error.");
            request.setAttribute("error_message", ErrorMessage.get("database_story_create_error"));
            request.setAttribute("choices", choices);
            return Path.PAGE_CREATE_STORY;
        }

        // ajouter un ou plusieurs choix: créer les paragraphes enfants, insérer dans
        // Parent Section
        // parag_condition est le parent (choix inconditionnel) ou un autre paragraphe
        // conditionnel
        // si paragraphe final il est possible de ne pas faire de choix

        LOG.debug("CreateStory Action finished");

        return Path.REDIRECT_SHOW_USER_STORIES;
    }
}
