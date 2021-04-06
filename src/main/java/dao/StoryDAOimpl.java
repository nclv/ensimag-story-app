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
import models.Story;

public class StoryDAOimpl implements StoryDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_FIND_STORIES_USER_ID = "SELECT * FROM \"Story\" WHERE \"user_id\"=?";
    private final static String SQL_FIND_STORY_STORY_ID = "SELECT * FROM \"Story\" WHERE \"story_id\"=?";
    private final static String SQL_INSERT_STORY = "INSERT INTO \"Story\" (\"open\", \"published\", \"user_id\") VALUES (?, ?, ?)";
    private final static String SQL_UPDATE_STORY = "UPDATE \"Story\" SET \"open\"=?, \"published\"=?, \"user_id\"=? WHERE \"story_id\"=?";
    private final static String SQL_FIND_ALL_OPEN_PUBLISHED_STORIES = "SELECT * FROM \"Story\" WHERE \"open\"=1 AND \"published\"=1";
    private final static String SQL_FIND_ALL_PUBLISHED_STORIES = "SELECT * FROM \"Story\" WHERE \"published\"=1";

    private final static String SQL_GET_STORY_ID = "SELECT \"STORY_STORY_ID_SEQ\".currval FROM DUAL";

    private static Connection connection = null;

    public static void setConnection(Connection connection) {
        StoryDAOimpl.connection = connection;
    }

    @Override
    public long saveStory(Story story) throws SQLException {
        long id = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STORY)) {
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
        }

        return id;
    }

    @Override
    public Story findStory(long id) throws SQLException {
        Story story = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_STORY_STORY_ID)) {
            preparedStatement.setLong(1, id);

            story = getStory(preparedStatement);
        }

        return story;
    }

    @Override
    public void updateStory(Story story) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_STORY)) {
            preparedStatement.setInt(1, story.isOpen() ? 1 : 0);
            preparedStatement.setInt(2, story.isPublished() ? 1 : 0);
            preparedStatement.setLong(3, story.getUser_id());
            preparedStatement.setLong(4, story.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Story> findStories(long userId) throws SQLException {
        List<Story> stories = new ArrayList<Story>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_STORIES_USER_ID)) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Story story = null;
                while (resultSet.next()) {
                    story = getStory(resultSet);
                    stories.add(story);
                }
                LOG.error(stories);
            }
        }

        return stories;
    }

    @Override
    public List<Story> findAllOpenPublishedStories() throws SQLException {
        List<Story> stories = new ArrayList<Story>();
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_OPEN_PUBLISHED_STORIES)) {

            Story story = null;
            while (resultSet.next()) {
                story = getStory(resultSet);
                stories.add(story);
            }
            LOG.error(stories);

        }

        return stories;
    }

    @Override
    public List<Story> findAllPublishedStories() throws SQLException {
        List<Story> stories = new ArrayList<Story>();
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_PUBLISHED_STORIES)) {

            Story story = null;
            while (resultSet.next()) {
                story = getStory(resultSet);
                stories.add(story);
            }
            LOG.error(stories);
        }

        return stories;
    }

    private Story getStory(PreparedStatement preparedStatement) throws SQLException {
        Story story = null;

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                story = getStory(resultSet);
            }
        }

        return story;
    }

    private Story getStory(ResultSet resultSet) throws SQLException {
        return Story.builder().id(resultSet.getLong(DatabaseFields.STORY_ID))
                .user_id(resultSet.getLong(DatabaseFields.USER_ID))
                .open((resultSet.getInt(DatabaseFields.STORY_OPEN) == 1))
                .published((resultSet.getInt(DatabaseFields.STORY_PUBLISHED) == 1)).build();
    }
}
