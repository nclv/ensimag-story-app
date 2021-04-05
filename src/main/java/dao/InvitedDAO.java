package dao;

import java.util.List;

import models.Invited;

public interface InvitedDAO {
    int saveInvited(Invited invited);
    List<Invited> findAllInvitedUsers(long storyId);
    int removeInvited(Invited invited);
}
