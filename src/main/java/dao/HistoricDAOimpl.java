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
import models.Historic;

public class HistoricDAOimpl implements HistoricDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_HISTORIC = "INSERT INTO \"Historique\" (\"user_id\", \"story_id\", \"para_id\") VALUES (?, ?, ?)";
    private final static String SQL_FIND_ALL_HISTORIC = "SELECT * FROM \"Historic\" WHERE \"user_id\"=? AND \"story_id\"=?";;

    private final static String SQL_GET_HISTORIC_ID = "SELECT \"HISTORIQUE_HISTORIC_ID_SEQ\".currval FROM DUAL";

    private Connection connection = null;

    public HistoricDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long saveHistoric(Historic historic) throws SQLException {
        long id = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_HISTORIC)) {
            preparedStatement.setLong(1, historic.getUser_id());
            preparedStatement.setLong(2, historic.getStory_id());
            preparedStatement.setLong(3, historic.getParagraphe_id());

            preparedStatement.executeUpdate();

            try (PreparedStatement ps = connection.prepareStatement(SQL_GET_HISTORIC_ID);
                    ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
        }

        return id;
    }

    @Override
    public List<Historic> findAllHistoric(long userId, long storyId) throws SQLException {
        List<Historic> history = new ArrayList<Historic>();
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
