package filters;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
public class CreateStoryValidationFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String actionName = req.getMethod() + "/" + req.getParameter("action");
        boolean canFilter = actionName.equals("POST/create_story");
        LOG.error(actionName);
        LOG.error(canFilter);

        if (canFilter) {
            boolean isFinal = req.getParameter("is_final").equals("final") ? true : false;
            List<String> choices = Collections.list(req.getParameterNames()).stream()
                    .filter(parameterName -> parameterName.startsWith("choice_")).map(request::getParameter)
                    .collect(Collectors.toList());

            if (request.getParameter("create_and_publish") != null && isFinal == false) {
                LOG.error("Your can't published a story without any final paragraphe.");

                request.setAttribute("error_message", "You need to have a final paragraphe to publish your story.");
                request.setAttribute("choices", choices);
                req.getRequestDispatcher(Path.PAGE_CREATE_STORY).include(req, resp);
            } else if (Validation.loggedIn(req, resp, Path.PAGE_LOGIN)
                    && Validation.content(req, resp, Path.PAGE_CREATE_STORY)) {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }
}
