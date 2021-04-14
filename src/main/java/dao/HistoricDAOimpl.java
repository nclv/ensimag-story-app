package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Historic;

public class HistoricDAOimpl implements HistoricDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_HISTORIC = "INSERT INTO \"Historique\" (\"user_id\", \"story_id\", \"para_id\", \"historic_id\") VALUES (?, ?, ?, ?)";
    private final static String SQL_FIND_ALL_HISTORIC = "SELECT * FROM \"Historique\" WHERE \"user_id\"=? AND \"story_id\"=? ORDER BY \"historic_id\"";
    private final static String SQL_REMOVE_USER_STORY_HISTORY = "DELETE FROM \"Historique\" WHERE \"user_id\"=? AND \"story_id\"=?";


    private Connection connection = null;

    public HistoricDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveHistoric(Historic historic) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_HISTORIC)) {
            preparedStatement.setLong(1, historic.getUser_id());
            preparedStatement.setLong(2, historic.getStory_id());
            preparedStatement.setLong(3, historic.getParagraphe_id());
            preparedStatement.setLong(4, historic.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void saveHistory(List<Historic> history) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_HISTORIC)) {

            for (Historic historic : history) {
                preparedStatement.setLong(1, historic.getUser_id());
                preparedStatement.setLong(2, historic.getStory_id());
                preparedStatement.setLong(3, historic.getParagraphe_id());
                preparedStatement.setLong(4, historic.getId());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    @Override
    public void removeAllHistoric(long userId, long storyId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_USER_STORY_HISTORY)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, storyId);

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Historic> findAllHistoric(long userId, long storyId) throws SQLException {
        List<Historic> history = new LinkedList<Historic>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_HISTORIC)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, storyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Historic paragraphe = null;
                while (resultSet.next()) {
                    paragraphe = getHistoric(resultSet);
                    history.add(paragraphe);
                }
            }
        }

        return history;
    }

    private Historic getHistoric(ResultSet resultSet) throws SQLException {
        return Historic.builder().id(resultSet.getLong(DatabaseFields.HISTORIC_ID))
                .user_id(resultSet.getLong(DatabaseFields.USER_ID)).story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .paragraphe_id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID)).build();
    }
}
