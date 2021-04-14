package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DAOManager;
import dao.HistoricDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Historic;
import models.User;
import utils.DatabaseManager;
import utils.ErrorMessage;
import utils.Path;

public class SaveHistoryAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    private static final long serialVersionUID = -7017999796677536348L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.error("SaveHistory Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");
        LOG.error(connectedUser);

        // Récupération de l'ID de la story
        String storyIdString = request.getParameter("story_id");
        long storyId = Long.parseLong(storyIdString);

        List<Historic> history = (LinkedList<Historic>) session.getAttribute("history");
        if (history == null || history.isEmpty()) {
            LOG.error("Empty history.");
            request.setAttribute("error_message", ErrorMessage.get("empty_history"));
            return Path.PAGE_ERROR;
        }

        // Database operations
        Optional<Connection> connection = DatabaseManager.getConnection();
        if (connection.isEmpty()) {
            request.setAttribute("error_message", ErrorMessage.get("connection_error"));
            return Path.PAGE_ERROR;
        }

        DAOManager daoManager = new DAOManager(connection.get());

        Object result = daoManager.transactionAndClose((daoFactory) -> {
            HistoricDAO historicDAO = daoFactory.getHistoricDAO();

            historicDAO.removeAllHistoric(connectedUser.getId(), storyId);

            long id = 1;
            for (Historic historic : history) {
                // si l'utilisateur se connecte après avoir commencé la lecture
                historic.setUser_id(connectedUser.getId());
                historic.setId(id);
                id++;
            }
            historicDAO.saveHistory(history);

            session.setAttribute("history", history);

            return true;
        });
        if (result == null) {
            LOG.error("Database query error.");
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }
        if (!(boolean) result) {
            return Path.PAGE_ERROR;
        }

        LOG.error("SaveHistory Action finished");

        return Path.REDIRECT_READ_STORY + "&story_id=" + storyIdString;
    }
}
