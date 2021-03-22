package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.Resource;
import models.DatabaseFields;
import models.User;

public class UserDAOimpl implements UserDAO {

    private static final Logger LOG = LogManager.getLogger();

    @Resource(name = "jdbc/story-app")
    private DataSource dataSource;

    private final static String SQL_FIND_USER_USER_ID = "SELECT * FROM \"User\" WHERE \"user_id\"=?";
    private final static String SQL_FIND_USER_USERNAME = "SELECT * FROM \"User\" WHERE \"username\"=?";
    private final static String SQL_INSERT_USER = "INSERT INTO \"User\" (\"username\", \"password\") VALUES (?, ?)";

    @Override
    public long saveUser(User user) {
        long id = -1;
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                id = resultSet.getLong(1);

        } catch (Exception e) {
            LOG.error("Failed inserting user", e);
        }

        return id;
    }

    @Override
    public User findUser(String username) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_USERNAME)) {
            preparedStatement.setString(1, username);

            user = getUser(preparedStatement);
        } catch (Exception e) {
            LOG.error("Failed querying user by " + DatabaseFields.USER_USERNAME, e);
        }

        return user;
    }

    @Override
    public User findUser(long id) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_USER_ID)) {
            preparedStatement.setLong(1, id);

            user = getUser(preparedStatement);
        } catch (Exception e) {
            LOG.error("Failed querying user by " + DatabaseFields.USER_ID, e);
        }

        return user;
    }

    private User getUser(PreparedStatement preparedStatement) throws SQLException {
        User user = null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user = User.builder().id(resultSet.getLong(DatabaseFields.USER_ID))
                    .name(resultSet.getString(DatabaseFields.USER_USERNAME))
                    .password(resultSet.getString(DatabaseFields.USER_PASSWORD)).build();
        }
        resultSet.close();

        return user;
    }
}
