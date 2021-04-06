package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.User;

public class UserDAOimpl implements UserDAO {

    private static final Logger LOG = LogManager.getLogger();

    // @Resource(name = "jdbc/story-app")
    // private DataSource dataSource;

    private final static String SQL_FIND_USER_USER_ID = "SELECT * FROM \"User\" WHERE \"user_id\"=?";
    private final static String SQL_FIND_USER_USERNAME = "SELECT * FROM \"User\" WHERE \"username\"=?";
    private final static String SQL_INSERT_USER = "INSERT INTO \"User\" (\"username\", \"password\") VALUES (?, ?)";
    private final static String SQL_UPDATE_USER = "UPDATE \"User\" SET \"username\"=?, \"password\"=? WHERE \"user_id\"=?";
    private final static String SQL_FIND_ALL_USERS_EXCEPT = "SELECT * FROM \"User\" WHERE \"user_id\"!=?";
    private final static String SQL_FIND_USERS = "SELECT * FROM \"User\" WHERE \"user_id\" in (%s)";

    private final static String SQL_GET_USER_ID = "SELECT \"USER_USER_ID_SEQ\".currval FROM DUAL";

    private static Connection connection = null;

    public static void setConnection(Connection connection) {
        UserDAOimpl.connection = connection;
    }

    @Override
    public long saveUser(User user) throws SQLException {
        long id = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.executeUpdate();

            try (PreparedStatement ps = connection.prepareStatement(SQL_GET_USER_ID);
                    ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
        }

        return id;
    }

    @Override
    public User findUser(String username) throws SQLException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_USERNAME)) {
            preparedStatement.setString(1, username);

            user = getUser(preparedStatement);
        }

        return user;
    }

    @Override
    public User findUser(long id) throws SQLException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_USER_ID)) {
            preparedStatement.setLong(1, id);

            user = getUser(preparedStatement);
        }

        return user;
    }

    @Override
    public List<User> findAllUsersExcept(long userId) throws SQLException {
        List<User> users = new ArrayList<User>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_USERS_EXCEPT)) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                User user = null;
                while (resultSet.next()) {
                    user = getUser(resultSet);
                    users.add(user);
                }
            }
        }

        return users;
    }

    @Override
    public List<User> findUsers(Set<Long> userIds) throws SQLException {
        List<User> users = new ArrayList<User>();
        String SQL_FIND_USERS_FORMAT = String.format(SQL_FIND_USERS, preparePlaceHolders(userIds.size()));

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USERS_FORMAT)) {

            setValues(preparedStatement, userIds.toArray());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                User user = null;
                while (resultSet.next()) {
                    user = getUser(resultSet);
                    users.add(user);
                }
            }
        }

        return users;
    }

    private static String preparePlaceHolders(int length) {
        return String.join(",", Collections.nCopies(length, "?"));
    }

    private static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());

            preparedStatement.executeUpdate();
        }
    }

    private User getUser(PreparedStatement preparedStatement) throws SQLException {
        User user = null;

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                user = getUser(resultSet);
            }
        }

        return user;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        return User.builder().id(resultSet.getLong(DatabaseFields.USER_ID))
                .name(resultSet.getString(DatabaseFields.USER_USERNAME))
                .password(resultSet.getString(DatabaseFields.USER_PASSWORD)).build();
    }
}
