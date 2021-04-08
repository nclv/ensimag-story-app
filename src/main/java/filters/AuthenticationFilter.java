package filters;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.ActionsMap;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.Path;

@WebFilter(urlPatterns = { "/jsp/authenticated_user/*", "/WEB-INF/jsp/authenticated_user/*", "/controller" })
public class AuthenticationFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String forward = Path.PAGE_LOGIN;

        // On filtre si l'action du controller le nécessite et on redirige sur
        // actionName après login
        String actionName = req.getParameter("action");
        boolean loginNeeded = ActionsMap.authenticatedActionsContains(actionName);
        if (actionName == null && !req.getRequestURI().contains("/controller")) {
            actionName = FilenameUtils.getBaseName(req.getRequestURI());
            loginNeeded = true; // on doit se login pour toute page autre page qu'un controller
        }
        LOG.error(actionName);
        LOG.error(loginNeeded);

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        LOG.error(loggedIn);
        if (loggedIn || !loginNeeded) {
            chain.doFilter(req, resp);
        } else {
            // Get all parameters from the url
            Enumeration<String> parameterNames = req.getParameterNames();
            String parameters = "";
            while (parameterNames.hasMoreElements()) {
                String key = (String) parameterNames.nextElement();
                String val = req.getParameter(key);
                parameters += key + "=" + val + "&";
                LOG.error("A= <" + key + "> Value<" + val + ">");
            }
            // req.getRequestDispatcher(forward).forward(req, resp);
            resp.sendRedirect(req.getContextPath() + forward + "?redirect="
                    + URLEncoder.encode("/controller?" + parameters, "UTF-8"));
        }
    }
}
