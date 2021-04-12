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
public class ReadStoryValidationFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String actionName = req.getMethod() + "/" + req.getParameter("action");
        boolean canFilter = actionName.equals("GET/read_story");
        LOG.error(actionName);
        LOG.error(canFilter);

        if (!canFilter || (canFilter && Validation.storyId(req, resp, Path.PAGE_ERROR)
                && Validation.published(req, resp, Path.PAGE_ERROR))) {
            chain.doFilter(req, resp);
        }
    }
}
