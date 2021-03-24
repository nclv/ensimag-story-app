package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Story;
import models.User;
import utils.DatabaseManager;

public class StoryDAOimpl implements StoryDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_FIND_STORY_STORY_ID = "SELECT * FROM \"Story\" WHERE \"story_id\"=?";
    private final static String SQL_INSERT_STORY = "INSERT INTO \"Story\" (\"open\", \"published\", \"user_id\") VALUES (?, ?)";

    private final static String SQL_GET_STORY_ID = "SELECT \"STORY_STORY_ID_SEQ\".currval FROM DUAL";

    @Override
    public long saveStory(Story story) {
        long id = -1;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STORY)) {
            preparedStatement.setInt(1, story.isOpen() ? 1 : 0);
            preparedStatement.setInt(2, story.isPublished() ? 1 : 0);
            preparedStatement.setLong(3, story.getUser_id());

            preparedStatement.executeUpdate();

            try (PreparedStatement ps = connection.prepareStatement(SQL_GET_STORY_ID);
                    ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
        } catch (Exception e) {
            LOG.error("Failed inserting story", e);
        }

        return id;
    }

    @Override
    public Story findStory(long id) {
        Story story = null;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_STORY_STORY_ID)) {
            preparedStatement.setLong(1, id);

            story = getStory(preparedStatement);
        } catch (Exception e) {
            LOG.error("Failed querying story by " + DatabaseFields.STORY_ID, e);
        }

        return story;
    }

    private Story getStory(PreparedStatement preparedStatement) throws SQLException {
        Story story = null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            story = Story.builder().id(resultSet.getLong(DatabaseFields.STORY_ID))
                    .user_id(resultSet.getLong(DatabaseFields.USER_ID))
                    .open((resultSet.getInt(DatabaseFields.STORY_OPEN) == 1))
                    .published((resultSet.getInt(DatabaseFields.STORY_PUBLISHED) == 1)).build();
        }
        resultSet.close();

        return story;
    }
}
