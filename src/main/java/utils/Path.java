package utils;

/**
 * Classe statique contenant les différents chemins des vues.
 */
public final class Path {
    private Path() {}

    public static final String JSP_PATH_ACCESSIBLE = "/jsp/";
    public static final String JSP_PATH_PRIVATE = "/WEB-INF/jsp/";

    /* Utilisé dans les servlets (Java) */
    public static final String PAGE_ERROR = JSP_PATH_ACCESSIBLE + "error.jsp";
    public static final String PAGE_ABOUT = JSP_PATH_ACCESSIBLE + "about.jsp";
    public static final String PAGE_LOGIN = JSP_PATH_ACCESSIBLE + "login.jsp";
    public static final String PAGE_REGISTER = JSP_PATH_ACCESSIBLE + "register.jsp";
    public static final String PAGE_CREATE_STORY = JSP_PATH_ACCESSIBLE + "authenticated_user/create_story.jsp";
    /* Only use in ShowUserStoriesAction, use REDIRECT_SHOW_USER_STORIES */
    public static final String PAGE_SHOW_USER_STORIES = JSP_PATH_ACCESSIBLE + "authenticated_user/show_user_stories.jsp";

    /* Only use in HomePageAction, use REDIRECT_HOME */
    public static final String PAGE_HOME = JSP_PATH_PRIVATE + "index.jsp";

    /* Utilisé dans les pages JSP */
    public static final String REDIRECT_HOME = "/controller?action=home";
    public static final String REDIRECT_LOGIN = "/controller?action=login";
    public static final String REDIRECT_LOGOUT = "/controller?action=logout";
    public static final String REDIRECT_REGISTER = "/controller?action=register";
    public static final String REDIRECT_CREATE_STORY = "/controller?action=create_story";
    public static final String REDIRECT_SHOW_USER_STORIES = "/controller?action=show_user_stories";

}
