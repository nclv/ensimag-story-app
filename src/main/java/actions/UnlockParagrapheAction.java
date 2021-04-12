package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.ParagrapheDAO;
import dao.StoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Paragraphe;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class UnlockParagrapheAction implements Action {

    private static final long serialVersionUID = -2320956764031525285L;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("UnlockParagraphe Action start");

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = request.getParameter("paragraphe_id");
        long paragrapheId = Long.parseLong(paragrapheIdString);

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.executeAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();

            // on peut get()
            Paragraphe paragraphe = paragrapheDAO.findParagraphe(storyId, paragrapheId).get();
            // la sequence généré par Oracle commence par 1. 
            // 0 ne représente aucun utilisateur.
            paragraphe.setUser_id(0);
            paragrapheDAO.updateParagraphe(paragraphe);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_unlock_paragraphe_error"));
            return Path.PAGE_ERROR;
        }

        LOG.debug("UnlockParagraphe Action finished");

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }
}
