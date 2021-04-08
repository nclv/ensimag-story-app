package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Redaction;

public class RedactionDAOimpl implements RedactionDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_REDACTION = "INSERT INTO \"Redaction\" (\"user_id\", \"story_id\", \"para_id\", \"is_validated\") VALUES (?, ?, ?, ?)";

    private Connection connection = null;

    public RedactionDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveRedaction(Redaction redaction) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_REDACTION)) {
            preparedStatement.setLong(1, redaction.getUser_id());
            preparedStatement.setLong(2, redaction.getStory_id());
            preparedStatement.setLong(3, redaction.getParagraphe_id());
            preparedStatement.setLong(4, redaction.isValidated() ? 1 : 0);

            preparedStatement.executeUpdate();
        }
    }

    private Redaction getRedaction(ResultSet resultSet) throws SQLException {
        return Redaction.builder().story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .user_id(resultSet.getLong(DatabaseFields.USER_ID))
                .paragraphe_id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID))
                .validated((resultSet.getInt(DatabaseFields.REDACTION_IS_VALIDATED) == 1)).build();
    }
}
