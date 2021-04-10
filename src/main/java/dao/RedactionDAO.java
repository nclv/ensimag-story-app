package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import models.Redaction;

public interface RedactionDAO {
    void saveRedaction(Redaction redaction) throws SQLException;
    void updateRedaction(Redaction redaction) throws SQLException;
    List<Redaction> findAllInvalidated(long userId) throws SQLException;
    Optional<Redaction> findRedaction(long userId, long storyId, long paragrapheId) throws SQLException;
}
