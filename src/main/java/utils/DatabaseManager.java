package utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DatabaseManager {

    private static final Logger LOG = LogManager.getLogger();

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context) initContext.lookup("java:/comp/env");

            DataSource dataSource = (DataSource) envContext.lookup("jdbc/story-app");

            connection = dataSource.getConnection();
        } catch (NamingException ex) {
            LOG.error("Failed while getting a JDBC connection", ex);
        }
        return connection;
    }

}
