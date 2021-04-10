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
import models.Paragraphe;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class RemoveParagrapheAction implements Action {

    private static final long serialVersionUID = 8321981128771217449L;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("RemoveParagraphe Action start");

        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        String paragrapheIdString = request.getParameter("paragraphe_id");
        long paragrapheId = Long.parseLong(paragrapheIdString);

        Paragraphe paragraphe = Paragraphe.builder().story_id(storyId).id(paragrapheId).build();

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.executeAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();

            paragrapheDAO.removeParagraphe(paragraphe);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_remove_paragraphe_error"));
            return Path.PAGE_ERROR;
        }

        LOG.debug("RemoveParagraphe Action finished");

        return Path.REDIRECT_SHOW_STORY + "&story_id=" + storyIdString;
    }

}
