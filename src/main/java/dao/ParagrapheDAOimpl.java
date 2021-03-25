package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Paragraphe;
import utils.DatabaseManager;

public class ParagrapheDAOimpl implements ParagrapheDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_FIND_PARAGRAPHE = "SELECT * FROM \"Paragraphe\" WHERE \"story_id\"=? AND \"para_id\"=?";
    private final static String SQL_INSERT_PARAGRAPHE = "INSERT INTO \"Paragraphe\" (\"story_id\", \"content\", \"is_final\") VALUES (?, ?, ?)";
    private final static String SQL_UPDATE_PARAGRAPHE = "UPDATE \"Paragraphe\" SET \"story_id\"=?, \"content\"=?, \"is_final\"=? WHERE \"para_id\"=?";
    private final static String SQL_FIND_ALL_STORY_PARAGRAPHES = "SELECT * FROM \"Paragraphe\" WHERE \"story_id\"=?";;

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

    @Override
    public void updateParagraphe(Paragraphe paragraphe) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PARAGRAPHE)) {
            preparedStatement.setLong(1, paragraphe.getStory_id());
            preparedStatement.setString(2, paragraphe.getContent());
            preparedStatement.setInt(3, paragraphe.is_final() ? 1 : 0);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Failed updating paragraphe", e);
        }
    }

    @Override
    public List<Paragraphe> findAllStoryParagraphes(long story_id) {
        List<Paragraphe> stories = new ArrayList<Paragraphe>();
        try (Connection connection = DatabaseManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_STORY_PARAGRAPHES)) {

            Paragraphe paragraphe = null;
            while (resultSet.next()) {
                paragraphe = getParagraphe(resultSet);
                stories.add(paragraphe);
            }
        } catch (SQLException e) {
            LOG.error("Failed querying story paragraphes", e);
        }

        return stories;
    }

    private Paragraphe getParagraphe(PreparedStatement preparedStatement) throws SQLException {
        Paragraphe paragraphe = null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            paragraphe = getParagraphe(resultSet);
        }
        resultSet.close();

        return paragraphe;
    }

    private Paragraphe getParagraphe(ResultSet resultSet) throws SQLException {
        return Paragraphe.builder().id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID))
                .story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .content(resultSet.getString(DatabaseFields.PARAGRAPHE_CONTENT))
                .is_final((resultSet.getInt(DatabaseFields.PARAGRAPHE_IS_FINAL) == 1)).build();
    }
}
