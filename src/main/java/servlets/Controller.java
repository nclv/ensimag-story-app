package servlets;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.Action;
import actions.ActionsMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 7353100868310154108L;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("doGet() was called");
        try {
            process(request, response);
        } catch (ServletException | IOException e) {
            LOG.error("Cannot process GET request", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("doGet() was called");
        try {
            process(request, response);
        } catch (ServletException | IOException e) {
            LOG.error("Cannot process POST request", e);
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("request processing started");

        String actionName = request.getParameter("action");
        LOG.trace("Request parameter: action --> " + actionName);

        Action action = ActionsMap.get(request);
        LOG.trace("Obtained action --> " + action);

        String forward = action.execute(request, response);
        LOG.error("Forward address --> " + forward);

        if (forward.contains("/controller")) {
            LOG.debug("Controller finished, now go to forward address with a redirect --> " + forward);
            response.sendRedirect(request.getContextPath() + forward);
        } else {
            LOG.debug("Controller finished, now go to forward address --> " + forward);
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }
}
