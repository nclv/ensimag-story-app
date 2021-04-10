package utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ErrorMessage {

    private static final Logger LOG = LogManager.getLogger();

    private ErrorMessage() {
    }

    private static final Map<String, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put("connection_error", "Could not open a connection to the database.");
        errorMessages.put("database_query_error", "Database query error.");
        errorMessages.put("database_story_create_error",
                "Error when creating your story. Fill the fields and submit your story again.");
        errorMessages.put("database_paragraphe_create_error",
                "Error when adding your paragraphe. Fill the fields and submit your paragraphe again.");
        errorMessages.put("database_invite_users_error", "Error when inviting users.");
        errorMessages.put("database_remove_invited_user_error", "Couldn't remove invited user.");
        errorMessages.put("database_update_password_error", "Database error. Please retry.");

        errorMessages.put("username_empty", "Enter an user name.");
        errorMessages.put("password_empty", "Enter a password.");
        errorMessages.put("new_password_empty", "Enter a new password.");
        errorMessages.put("new_password_confirmation_empty", "Enter a new password confirmation.");
        errorMessages.put("password_confirmation_error", "Enter the same password twice.");
        errorMessages.put("userId_empty", "user_id is null.");
        errorMessages.put("userId_non_numeric", "user_id is not a number.");
        errorMessages.put("userId_inexistent", "user_id doesn't exist.");
        errorMessages.put("storyId_empty", "story_id is null.");
        errorMessages.put("storyId_non_numeric", "story_id is not a number.");
        errorMessages.put("storyId_inexistent", "story_id doesn't exist.");
        errorMessages.put("paragrapheId_empty", "paragraphe_id is null.");
        errorMessages.put("paragrapheId_non_numeric", "paragraphe_id is not a number.");
        errorMessages.put("paragrapheId_inexistent", "story_id, paragraphe_id doesn't exist.");
        errorMessages.put("open_story", "The story is open. Everyone is invited.");
        errorMessages.put("username_invalid", "Username is invalid.");
        errorMessages.put("password_invalid", "Password is invalid.");
        errorMessages.put("username_used", "This username is already used.");
        errorMessages.put("redaction_invalidated",
                "You are writing another paragraphe. You can only write a paragraphe at a time");
        errorMessages.put("story_not_published", "The story is not published and you are not it's author.");
        errorMessages.put("empty_content", "Enter a paragraphe content.");
        errorMessages.put("not_logged", "You are not logged in.");
        errorMessages.put("no_final_no_choices", "You can't create a paragraphe non final without a choice.");
        errorMessages.put("create_publish_no_final", "You need to have a final paragraphe to publish your story.");
        
        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of errors --> " + errorMessages.size());
    }

    public static String get(String errorId) {
        return errorMessages.get(errorId);
    }
}