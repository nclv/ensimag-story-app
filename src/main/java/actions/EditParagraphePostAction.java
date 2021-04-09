/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import dao.RedactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Paragraphe;
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

        boolean is_final = request.getParameter("is_final").equals("final") ? true : false;
        LOG.error("Final paragraphe: " + is_final);

        List<String> choices = Collections.list(request.getParameterNames()).stream()
                .filter(parameterName -> parameterName.startsWith("choice_")).map(request::getParameter)
                .collect(Collectors.toList());
        LOG.error(choices);

        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).id(paragrapheId).user_id(user.getId())
                .content(content).last(is_final).build();

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            request.setAttribute("choices", choices);
            return Path.PAGE_EDIT_PARAGRAPHE;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.executeAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            RedactionDAO redactionDAO = daoFactory.getRedactionDAO();

            paragrapheDAO.updateParagraphe(paragraphe);

            // Valider l'entrée crée dans EditParagrapheActionGet
            Redaction validated = Redaction.builder().user_id(user.getId()).story_id(storyId)
                    .paragraphe_id(paragrapheId).validated(true).build();
            redactionDAO.updateRedaction(validated);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_paragraphe_create_error"));
            return Path.PAGE_EDIT_PARAGRAPHE;
        }
        if (!(boolean) result) {
            return Path.PAGE_EDIT_PARAGRAPHE;
        }

        LOG.error("EditParagraphePost Action finished");
        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }
}
