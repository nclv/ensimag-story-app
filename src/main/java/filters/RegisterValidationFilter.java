package filters;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Path;

@WebFilter("/controller")
public class RegisterValidationFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String actionName = req.getMethod() + "/" + req.getParameter("action");
        boolean canFilter = actionName.equals("POST/register");
        LOG.error(actionName);
        LOG.error(canFilter);

        if (canFilter) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            // Validation requête
            if (username == null || username.trim().isEmpty()) {
                LOG.error("There is no username --> [" + username + "]");

                req.setAttribute("error_message", "Enter an user name.");
                req.getRequestDispatcher(Path.PAGE_REGISTER).include(req, resp);
            } else if (password == null || password.trim().isEmpty()) {
                LOG.error("There is no password --> [" + username + "]");

                req.setAttribute("error_message", "Enter a password.");
                req.getRequestDispatcher(Path.PAGE_REGISTER).include(req, resp);
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }
}
