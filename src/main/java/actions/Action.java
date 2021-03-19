package actions;

import java.io.IOException;
import java.io.Serializable;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Action extends Serializable {
    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
