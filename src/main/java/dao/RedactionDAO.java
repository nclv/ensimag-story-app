package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import models.Redaction;

public interface RedactionDAO {
    void saveRedaction(Redaction redaction) throws SQLException;
    void updateRedaction(Redaction redaction) throws SQLException;
    List<Redaction> findAllInvalidatedUser(long userId) throws SQLException;
    List<Redaction> findAllInvalidatedStory(long storyId) throws SQLException;
    List<Redaction> findAllInvalidatedParagraphe(long storyId, long paragrapheId) throws SQLException;
    Optional<Redaction> findRedaction(long userId, long storyId, long paragrapheId) throws SQLException;
}
