package dao;

import java.sql.SQLException;
import java.util.List;

import models.Invited;

public interface InvitedDAO {
    void saveInvited(Invited invited) throws SQLException;
    List<Invited> findAllInvitedUsers(long storyId) throws SQLException;
    void removeInvited(Invited invited) throws SQLException;
}
