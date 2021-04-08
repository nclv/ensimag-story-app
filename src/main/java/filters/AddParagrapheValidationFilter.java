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

@WebFilter("/controller")
public class AddParagrapheValidationFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String actionName = req.getMethod() + "/" + req.getParameter("action");
        boolean canFilter = actionName.equals("POST/add_paragraphe");
        LOG.error(actionName);
        LOG.error(canFilter);

        if (canFilter) {
            String content = req.getParameter("paragraphe_content");
            List<String> choices = Collections.list(req.getParameterNames()).stream()
                    .filter(parameterName -> parameterName.startsWith("choice_")).map(request::getParameter)
                    .collect(Collectors.toList());

            if (content == null || content.trim().isEmpty()) {
                LOG.error("There is no content --> [" + content + "]");

                request.setAttribute("error_message", "Enter a first paragraphe.");
                request.setAttribute("choices", choices);
                req.getRequestDispatcher(Path.PAGE_ADD_PARAGRAPHE).include(req, resp);
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }
}
