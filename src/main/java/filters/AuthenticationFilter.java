package filters;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
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
import jakarta.servlet.http.HttpSession;
import utils.Path;

@WebFilter("/jsp/authenticated_user/*")
public class AuthenticationFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String forward = Path.PAGE_LOGIN;

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        if (loggedIn) {
            chain.doFilter(req, resp);
        } else {
            // req.getRequestDispatcher(forward).forward(req, resp);
            resp.sendRedirect(req.getContextPath() + forward + "?redirect=/controller?action="
                    + FilenameUtils.getBaseName(req.getRequestURI()));
        }
    }
}
