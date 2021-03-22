package utils;

/**
 * Classe statique contenant les différents chemins des vues.
 */
public final class Path {
    private Path() {}

    public static final String PAGE_ERROR = "jsp/error.jsp";
    public static final String PAGE_HOME = "jsp/index.jsp";
    public static final String PAGE_ABOUT = "jsp/about.jsp";
    public static final String PAGE_LOGIN = "jsp/login.jsp";
    public static final String PAGE_REGISTER = "jsp/register.jsp";

    public static final String APP = "/story-app";

    public static final String REDIRECT_LOGIN = APP + "/controller?action=login";
    public static final String REDIRECT_LOGOUT = APP + "/controller?action=logout";
    public static final String REDIRECT_REGISTER = APP + "/controller?action=register";
}
