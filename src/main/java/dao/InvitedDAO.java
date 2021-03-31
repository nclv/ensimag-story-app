package dao;

import java.util.List;

import models.Invited;

public interface InvitedDAO {
    void saveInvited(Invited invited);
    List<Invited> findAllInvitedUsers(long storyId);
}
