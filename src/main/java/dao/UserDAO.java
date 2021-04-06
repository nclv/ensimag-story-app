package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import models.User;

public interface UserDAO {
    long saveUser(User user) throws SQLException;
    User findUser(String username) throws SQLException;
    User findUser(long id) throws SQLException;
    void updateUser(User user) throws SQLException;
    List<User> findAllUsersExcept(long userId) throws SQLException;
    List<User> findUsers(Set<Long> userIds) throws SQLException;
}
