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
import utils.Validation;

@WebFilter("/controller")
public class UnlockParagrapheValidationFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String actionName = req.getMethod() + "/" + req.getParameter("action");
        boolean canFilter = actionName.equals("GET/unlock_paragraphe");
        LOG.error(actionName);
        LOG.error(canFilter);

        if (!canFilter || (canFilter && Validation.loggedIn(req, resp, Path.PAGE_LOGIN)
                && Validation.storyId(req, resp, Path.PAGE_ERROR)
                && Validation.paragrapheId(req, resp, Path.PAGE_ERROR)
                && Validation.isAuthor(req, resp, Path.PAGE_ERROR))) {
            chain.doFilter(req, resp);
        }
    }
}
