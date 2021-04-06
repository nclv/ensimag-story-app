package utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Validation {

    private static final Logger LOG = LogManager.getLogger();

    public static boolean UsernamePassword(HttpServletRequest req, HttpServletResponse resp, String forwardPage)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // Validation requête
        boolean valid = true;
        if (username == null || username.trim().isEmpty()) {
            LOG.error("There is no username --> [" + username + "]");
            valid = false;

            req.setAttribute("error_message", "Enter an user name.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        } else if (password == null || password.trim().isEmpty()) {
            LOG.error("There is no password --> [" + username + "]");
            valid = false;

            req.setAttribute("error_message", "Enter a password.");
            req.getRequestDispatcher(forwardPage).include(req, resp);
        }
        return valid;
    }
}
