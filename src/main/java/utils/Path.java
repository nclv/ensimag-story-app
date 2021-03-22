package utils;

/**
 * Classe statique contenant les différents chemins des vues.
 */
public final class Path {
    private Path() {}

    public static final String PAGE_ERROR = "jsp/error.jsp";
    public static final String PAGE_HOME = "jsp/home.jsp";
    public static final String PAGE_ABOUT = "jsp/about.jsp";
    public static final String PAGE_LOGIN = "jsp/login.jsp";

    public static final String REDIRECT_HOME = "/controller?action=home";
    public static final String REDIRECT_LOGIN = "/controller?action=login";
}
