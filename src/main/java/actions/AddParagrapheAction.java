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

public class AddParagrapheAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 254668985512218466L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("AddParagraphe Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String content = request.getParameter("paragraphe_content");

        boolean is_final = request.getParameter("is_final").equals("final") ? true : false;
        LOG.error("Final paragraphe: " + is_final);

        List<String> choices = Collections.list(request.getParameterNames()).stream()
                .filter(parameterName -> parameterName.startsWith("choice_")).map(request::getParameter)
                .collect(Collectors.toList());
        LOG.error(choices);

        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).user_id(user.getId()).content(content)
                .last(is_final).build();
        
        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            request.setAttribute("choices", choices);
            return Path.PAGE_ADD_PARAGRAPHE;
        }


        DAOManager daoManager = new DAOManager(connection.get());
        
        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            RedactionDAO redactionDAO = daoFactory.getRedactionDAO();

            // On vérifie que l'utilisateur actuel n'édite pas un autre paragraphe.
            Optional<Redaction> invalidated = redactionDAO.getInvalidated(user.getId());
            boolean valid = validation(request, invalidated);
            if (!valid) {
                return valid;
            }

            long paragrapheId = paragrapheDAO.saveParagraphe(paragraphe);
            LOG.error(paragrapheId + " " + paragraphe);
            
            return true;
        });
        if (result.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("database_paragraphe_create_error"));
            return Path.PAGE_ADD_PARAGRAPHE;
        }
        if (!(boolean) result.get()) {
            return Path.PAGE_ADD_PARAGRAPHE;
        }

        LOG.debug("AddParagraphe Action finished");
        return Path.PAGE_SHOW_STORY;
    }

    private boolean validation(HttpServletRequest request, Optional<Redaction> invalidated) {
        boolean valid = true;
        if (invalidated.isPresent()) {
            LOG.error("You are writing another paragraphe: " + invalidated);

            request.setAttribute("error_message", ErrorMessage.get("redaction_invalidated"));
            valid = false;
        }
        return valid;
    }
}
