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
import models.Invited;

public class InvitedDAOimpl implements InvitedDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_INVITED = "INSERT INTO \"Invited\" (\"user_id\", \"story_id\", \"date\") VALUES (?, ?, ?)";
    private final static String SQL_FIND_INVITED_STORY_ID = "SELECT * FROM \"Invited\" WHERE \"story_id\"=?";
    private final static String SQL_REMOVE_INVITED = "DELETE FROM \"Invited\" WHERE \"user_id\"=? AND \"story_id\"=?";

    private static Connection connection = null;

    public static void setConnection(Connection connection) {
        InvitedDAOimpl.connection = connection;
    }

    @Override
    public void saveInvited(Invited invited) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INVITED)) {
            preparedStatement.setLong(1, invited.getUser_id());
            preparedStatement.setLong(2, invited.getStory_id());
            preparedStatement.setDate(3, invited.getDate());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Invited> findAllInvitedUsers(long storyId) throws SQLException {
        List<Invited> invitedList = new ArrayList<Invited>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_INVITED_STORY_ID)) {
            preparedStatement.setLong(1, storyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Invited story = null;
                while (resultSet.next()) {
                    story = getInvited(resultSet);
                    invitedList.add(story);
                }
                LOG.error(invitedList);
            }
        }

        return invitedList;
    }

    @Override
    public void removeInvited(Invited invited) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_INVITED)) {
            preparedStatement.setLong(1, invited.getUser_id());
            preparedStatement.setLong(2, invited.getStory_id());

            preparedStatement.executeUpdate();
        }
    }

    private Invited getInvited(ResultSet resultSet) throws SQLException {
        return Invited.builder().user_id(resultSet.getLong(DatabaseFields.USER_ID))
                .story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .date(resultSet.getDate(DatabaseFields.INVITED_DATE)).build();
    }
}
