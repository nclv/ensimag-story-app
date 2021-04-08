package dao;

import java.sql.SQLException;

import models.Redaction;

public interface RedactionDAO {
    void saveRedaction(Redaction redaction) throws SQLException;
}
