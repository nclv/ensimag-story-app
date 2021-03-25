package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Paragraphe;
import utils.DatabaseManager;

public class ParagrapheDAOimpl implements ParagrapheDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_FIND_PARAGRAPHE = "SELECT * FROM \"Paragraphe\" WHERE \"story_id\"=? AND \"para_id\"=?";
    private final static String SQL_INSERT_PARAGRAPHE = "INSERT INTO \"Paragraphe\" (\"story_id\", \"content\", \"is_final\") VALUES (?, ?, ?)";

    private final static String SQL_GET_PARAGRAPHE_ID = "SELECT \"PARAGRAPHE_PARA_ID_SEQ\".currval FROM DUAL";

    @Override
    public long saveParagraphe(Paragraphe paragraphe) {
        long id = -1;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_PARAGRAPHE)) {
            preparedStatement.setLong(1, paragraphe.getStory_id());
            preparedStatement.setString(2, paragraphe.getContent());
            preparedStatement.setInt(3, paragraphe.is_final() ? 1 : 0);

            preparedStatement.executeUpdate();

            try (PreparedStatement ps = connection.prepareStatement(SQL_GET_PARAGRAPHE_ID);
                    ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
        } catch (Exception e) {
            LOG.error("Failed inserting paragraphe", e);
        }

        return id;
    }

    @Override
    public Paragraphe findParagraphe(long story_id, long paragraphe_id) {
        Paragraphe paragraphe = null;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PARAGRAPHE)) {
            preparedStatement.setLong(1, story_id);
            preparedStatement.setLong(2, paragraphe_id);

            paragraphe = getParagraphe(preparedStatement);
        } catch (Exception e) {
            LOG.error("Failed querying paragraphe by (" + DatabaseFields.STORY_ID + ", " + DatabaseFields.PARAGRAPHE_ID
                    + ")", e);
        }

        return paragraphe;
    }

    private Paragraphe getParagraphe(PreparedStatement preparedStatement) throws SQLException {
        Paragraphe paragraphe = null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            paragraphe = Paragraphe.builder().id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID))
                    .story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                    .content(resultSet.getString(DatabaseFields.PARAGRAPHE_CONTENT))
                    .is_final((resultSet.getInt(DatabaseFields.PARAGRAPHE_IS_FINAL) == 1)).build();
        }
        resultSet.close();

        return paragraphe;
    }
}
