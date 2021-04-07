/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.ParagrapheDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Paragraphe;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

/**
 *
 * @author vincent
 */
public class AddParagrapheAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 254668985512218466L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("AddParagraphe Action starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // for POST request on /controller?action=create_story
        // if (user == null) {
        //     request.setAttribute("error_message", "There is no connected user.");
        //     return Path.PAGE_ERROR;
        // }

        boolean is_final = request.getParameter("is_final").equals("final") ? true : false;
        String content = request.getParameter("paragraphe_content");

        // if (content == null || content.trim().isEmpty()) {
        //     LOG.error("There is no content --> [" + content + "]");

        //     request.setAttribute("error_message", "Enter a paragraphe.");
        //     return Path.PAGE_ADD_PARAGRAPHE;
        // }

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).user_id(user.getId()).content(content)
                .last(is_final).build();
        
        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ADD_PARAGRAPHE;
        }

        DAOManager daoManager = new DAOManager(connection.get());
        
        Optional<Object> result = daoManager.executeAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            long paragrapheId = paragrapheDAO.saveParagraphe(paragraphe);
            LOG.error(paragrapheId + " " + paragraphe);
            return true;
        });
        if (result.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("database_paragraphe_create_error"));
            return Path.PAGE_ADD_PARAGRAPHE;
        }

        LOG.debug("AddParagraphe Action finished");
        return Path.REDIRECT_SHOW_USER_STORIES;
    }
}
