package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Redaction;

public class RedactionDAOimpl implements RedactionDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_REDACTION = "INSERT INTO \"Redaction\" (\"user_id\", \"story_id\", \"para_id\", \"is_validated\") VALUES (?, ?, ?, ?)";
    private final static String SQL_UPDATE_REDACTION = "UPDATE \"Redaction\" SET \"is_validated\"=? WHERE \"user_id\"=? AND \"story_id\"=? AND \"para_id\"=?";
    private final static String SQL_FIND_INVALIDATED = "SELECT * FROM \"Redaction\" WHERE \"user_id\"=? AND \"is_validated\"=0";

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

    @Override
    public void updateRedaction(Redaction redaction) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_REDACTION)) {
            preparedStatement.setLong(1, redaction.isValidated() ? 1 : 0);
            preparedStatement.setLong(2, redaction.getUser_id());
            preparedStatement.setLong(3, redaction.getStory_id());
            preparedStatement.setLong(4, redaction.getParagraphe_id());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Redaction> getInvalidated(long userId) throws SQLException {
        List<Redaction> redactions = new ArrayList<Redaction>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_INVALIDATED)) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Redaction redaction = null;
                while (resultSet.next()) {
                    redaction = getRedaction(resultSet);
                    redactions.add(redaction);
                }
            }
        }

        return redactions;
    }

    private Redaction getRedaction(PreparedStatement preparedStatement) throws SQLException {
        Redaction redaction = null;

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                redaction = getRedaction(resultSet);
            }
        }

        return redaction;
    }

    private Redaction getRedaction(ResultSet resultSet) throws SQLException {
        return Redaction.builder().story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .user_id(resultSet.getLong(DatabaseFields.USER_ID))
                .paragraphe_id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID))
                .validated((resultSet.getInt(DatabaseFields.REDACTION_IS_VALIDATED) == 1)).build();
    }
}
