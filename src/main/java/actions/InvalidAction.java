package actions;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Path;

public class InvalidAction implements Action {

    private static final long serialVersionUID = -5113433303924555307L;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("error_message", "There is no such a command");

        return Path.PAGE_ERROR;
    }

}
