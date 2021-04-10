package actions;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
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

public class EditParagrapheGetAction implements Action {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -7529000845432412948L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("EditParagrapheGet Action starts");

        HttpSession session = request.getSession();
        User connectedUser = (User) session.getAttribute("user");

        // Récupération de l'ID de la story et du paragraphe
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

        Object result = daoManager.transactionAndClose((daoFactory) -> {
            ParagrapheDAO paragrapheDAO = daoFactory.getParagrapheDAO();
            ParentSectionDAO parentSectionDAO = daoFactory.getParentSectionDAO();
            RedactionDAO redactionDAO = daoFactory.getRedactionDAO();

            // On récupère si le paragraphe a déjà été édité.
            Optional<Redaction> redactionOpt = redactionDAO.findRedaction(connectedUser.getId(), storyId, paragrapheId);
            Redaction redaction = redactionOpt.orElse(Redaction.builder().user_id(connectedUser.getId())
                    .story_id(storyId).paragraphe_id(paragrapheId).validated(false).build());

            if (redactionOpt.isEmpty()) {
                // créer une entrée non validée dans Redaction (GET)
                redactionDAO.saveRedaction(redaction);
            } else {
                // non vide, on update l'entrée actuelle
                redactionDAO.updateRedaction(redaction);
            }

            // l'existence du paragraphe est vérifiée dans les filtres
            Paragraphe paragraphe = paragrapheDAO.findParagraphe(storyId, paragrapheId).get();
            // construction des choix à partir des paragraphes enfants
            List<String> choices = parentSectionDAO.findChildrenParagraphe(storyId, paragrapheId).stream()
                    .map(ParentSection::getChoice_text).collect(Collectors.toList());

            setAttributes(request, paragraphe, choices);

            return true;
        });
        if (result == null) {
            request.setAttribute("error_message", ErrorMessage.get("database_query_error"));
            return Path.PAGE_ERROR;
        }

        LOG.error("EditParagrapheGet Action finished");

        return Path.PAGE_EDIT_PARAGRAPHE;
    }

    private void setAttributes(HttpServletRequest request, Paragraphe paragraphe, List<String> choices) {
        request.setAttribute("paragraphe_content", paragraphe.getContent());
        request.setAttribute("is_final", paragraphe.isLast() ? "final" : "non-final");
        request.setAttribute("choices", choices);
    }
}
