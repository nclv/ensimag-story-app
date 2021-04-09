package dao;

import java.sql.SQLException;
import java.util.List;

import models.Redaction;

public interface RedactionDAO {
    void saveRedaction(Redaction redaction) throws SQLException;
    void updateRedaction(Redaction redaction) throws SQLException;
    List<Redaction> getInvalidated(long userId) throws SQLException;
}
