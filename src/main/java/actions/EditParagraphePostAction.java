/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.ParagrapheDAO;
import dao.ParentSectionDAO;
import dao.RedactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Paragraphe;
import models.ParentSection;
import models.Redaction;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class EditParagraphePostAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 254668985512218466L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("EditParagraphePost Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = request.getParameter("paragraphe_id");
        long paragrapheId = Long.parseLong(paragrapheIdString);

        String content = request.getParameter("paragraphe_content");

        List<String> choices = Collections.list(request.getParameterNames()).stream()
                .filter(parameterName -> parameterName.startsWith("choice_")).map(request::getParameter)
                .filter(item -> !item.isEmpty()).collect(Collectors.toList());
        LOG.error(choices);

        String isFinalString = request.getParameter("is_final");

        boolean isFinal = false;
        if (isFinalString != null) {
            isFinal = isFinalString.equals("final") ? true : false;
        }
        LOG.error("Final paragraphe: " + isFinal);

        // String conditionnalParagrapheIdString = request.getParameter("conditionnal");

        // Récupération des paragraphes vers lesquels converger
        String[] convergeParagraphesIdsString = request.getParameterValues("converge");
        Set<Long> convergeParagraphesIds = Collections.emptySet();
        if (convergeParagraphesIdsString != null) {
            convergeParagraphesIds = Arrays.stream(convergeParagraphesIdsString).mapToLong(Long::valueOf).boxed()
                    .collect(Collectors.toSet());
        }

        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).id(paragrapheId).user_id(user.getId())
                .content(content).last(isFinal).build();

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            request.setAttribute("choices", choices);
            return Path.REDIRECT_EDIT_PARAGRAPHE + "&story_id=" + storyIdString + "&paregraphe_id="
                    + paragrapheIdString;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        final Set<Long> convergeParagraphesIdsFinal = convergeParagraphesIds;
        Object result = daoManager.transactionAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            RedactionDAO redactionDAO = daoFactory.getRedactionDAO();
            ParentSectionDAO parentSectionDAO = daoFactory.getParentSectionDAO();

            paragrapheDAO.updateParagraphe(paragraphe);

            ParentSection parentSection = ParentSection.builder().story_id(storyId).parent_story_id(storyId)
                    .parent_paragraphe_id(paragrapheId).paragraphe_conditionnel_story_id(storyId)
                    .paragraphe_conditionnel_id(paragrapheId).build();

            Set<Long> childParagraphesIds = parentSectionDAO.findChildrenParagraphe(storyId, paragrapheId).stream()
                    .map(ParentSection::getParagraphe_id).collect(Collectors.toSet());
            LOG.error(childParagraphesIds);

            // On ne garde que les paragraphes vers lesquels on ne converge pas déjà
            Set<Long> nonChildConvergeParagraphesIds = new HashSet<>(convergeParagraphesIdsFinal);
            nonChildConvergeParagraphesIds.removeAll(childParagraphesIds);

            // Ajout des paragraphes vers lesquels converger
            int index = 0;
            for (long convergeParagrapheId : nonChildConvergeParagraphesIds) {
                parentSection.setChoice_text("Converge to " + convergeParagrapheId);
                parentSection.setChoice_number(index);
                parentSection.setParagraphe_id(convergeParagrapheId);

                parentSectionDAO.saveParentSection(parentSection);
                index++;
            }

            // Ajout des choix supplémentaires
            long childParagrapheId;
            Paragraphe childParagraphe = Paragraphe.builder().story_id(storyId).user_id(user.getId())
                    .content("NO CONTENT YET").build();

            for (String choice : choices) {
                childParagrapheId = paragrapheDAO.saveParagraphe(childParagraphe);

                parentSection.setChoice_text(choice);
                parentSection.setChoice_number(index);
                parentSection.setParagraphe_id(childParagrapheId);

                parentSectionDAO.saveParentSection(parentSection);

                index++;
            }

            // Valider l'entrée crée dans EditParagrapheActionGet
            Redaction validated = Redaction.builder().user_id(user.getId()).story_id(storyId)
                    .paragraphe_id(paragrapheId).validated(true).build();
            redactionDAO.updateRedaction(validated);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_paragraphe_create_error"));
            return Path.REDIRECT_EDIT_PARAGRAPHE + "&story_id=" + storyIdString + "&paregraphe_id="
                    + paragrapheIdString;
        }

        LOG.error("EditParagraphePost Action finished");
        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }
}
