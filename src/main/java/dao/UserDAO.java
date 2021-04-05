package dao;

import java.util.List;
import java.util.Set;

import models.User;

public interface UserDAO {
    long saveUser(User user);
    User findUser(String username);
    User findUser(long id);
    int updateUser(User user);
    List<User> findAllUsersExcept(long userId);
    List<User> findUsers(Set<Long> userIds);

}
