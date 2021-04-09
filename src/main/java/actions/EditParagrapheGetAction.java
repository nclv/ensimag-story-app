package actions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Path;

public class EditParagrapheGetAction implements Action {

    private static final Logger LOG = LogManager.getLogger();
    
    private static final long serialVersionUID = -7529000845432412948L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("EditParagrapheGet Action starts");

        LOG.error("EditParagrapheGet Action finished");

        return Path.PAGE_EDIT_PARAGRAPHE;
    }
    
}
