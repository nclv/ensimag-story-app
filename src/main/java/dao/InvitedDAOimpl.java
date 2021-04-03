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
import utils.DatabaseManager;

public class InvitedDAOimpl implements InvitedDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_INVITED = "INSERT INTO \"Invited\" (\"user_id\", \"story_id\", \"date\") VALUES (?, ?, ?)";
    private final static String SQL_FIND_INVITED_STORY_ID = "SELECT * FROM \"Invited\" WHERE \"story_id\"=?";

    @Override
    public long saveInvited(Invited invited) {
        long err = -1;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INVITED)) {
            preparedStatement.setLong(1, invited.getUser_id());
            preparedStatement.setLong(2, invited.getStory_id());
            preparedStatement.setDate(3, invited.getDate());

            preparedStatement.executeUpdate();
            err = 0;
        } catch (Exception e) {
            LOG.error("Failed inserting invited", e);
        }
        return err;
    }

    @Override
    public List<Invited> findAllInvitedUsers(long storyId) {
        List<Invited> invitedList = new ArrayList<Invited>();
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_INVITED_STORY_ID)) {
            preparedStatement.setLong(1, storyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            Invited story = null;
            while (resultSet.next()) {
                story = getInvited(resultSet);
                invitedList.add(story);
            }
            LOG.error(invitedList);

            resultSet.close();
        } catch (SQLException e) {
            LOG.error("Failed querying user (" + storyId + ") invited", e);
        }

        return invitedList;
    }

    private Invited getInvited(ResultSet resultSet) throws SQLException {
        return Invited.builder().user_id(resultSet.getLong(DatabaseFields.USER_ID))
                .story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .date(resultSet.getDate(DatabaseFields.INVITED_DATE)).build();
    }
}
