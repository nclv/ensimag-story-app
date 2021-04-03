package dao;

import java.util.List;

import models.Invited;

public interface InvitedDAO {
    long saveInvited(Invited invited);
    List<Invited> findAllInvitedUsers(long storyId);
}
