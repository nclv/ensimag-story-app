package utils;

/**
 * Classe statique contenant les différents chemins des vues.
 */
public final class Path {
    private Path() {}

    /* Utilisé dans les servlets (Java) */
    public static final String PAGE_ERROR = "/jsp/error.jsp";
    public static final String PAGE_HOME = "/jsp/index.jsp";
    public static final String PAGE_ABOUT = "/jsp/about.jsp";
    public static final String PAGE_LOGIN = "/jsp/login.jsp";
    public static final String PAGE_REGISTER = "/jsp/register.jsp";
    public static final String PAGE_CREATE_STORY = "/jsp/authenticated_user/create_story.jsp";
    public static final String PAGE_SHOW_STORIES = "/jsp/authenticated_user/show_stories.jsp";

    /* Utilisé dans les pages JSP */
    public static final String REDIRECT_LOGIN = "/controller?action=login";
    public static final String REDIRECT_LOGOUT = "/controller?action=logout";
    public static final String REDIRECT_REGISTER = "/controller?action=register";
    public static final String REDIRECT_CREATE_STORY = "/controller?action=create_story";
}
