package dao;

import models.User;

public interface UserDAO {
    long saveUser(User user);
    User findUser(String username);
    User findUser(long id);
    int updateUser(User user);
}
