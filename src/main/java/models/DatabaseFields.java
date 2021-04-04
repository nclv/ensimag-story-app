package models;

public final class DatabaseFields {

    private DatabaseFields () {}
    
    public static final String USER_ID = "user_id";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSWORD = "password";

    public static final String STORY_ID = "story_id";
    public static final String STORY_OPEN = "open";
    public static final String STORY_PUBLISHED = "published";

    public static final String PARAGRAPHE_ID = "para_id";
    public static final String PARAGRAPHE_CONTENT = "content";
    public static final String PARAGRAPHE_IS_FINAL = "is_final";

    public static final String INVITED_DATE = "date";

    public static final String REDACTION_IS_VALIDATED = "is_validated";

    public static final String PARENT_SECTION_PARENT_STORY_ID = "parent_story_id";
    public static final String PARENT_SECTION_PARENT_PARA_ID = "parent_para_id";
    public static final String PARENT_SECTION_PARAG_CONDITION_STORY_ID = "parag_condition_story_id";
    public static final String PARENT_SECTION_PARAG_CONDITION_PARA_ID = "parag_condition_para_id";
    public static final String PARENT_SECTION_CHOICE_TEXT = "choice_text";
}