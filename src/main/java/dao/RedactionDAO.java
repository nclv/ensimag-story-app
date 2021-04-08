package dao;

import java.sql.SQLException;
import java.util.Optional;

import models.Redaction;

public interface RedactionDAO {
    void saveRedaction(Redaction redaction) throws SQLException;
    void updateRedaction(Redaction redaction) throws SQLException;
    Optional<Redaction> getInvalidated(long userId) throws SQLException;
}
