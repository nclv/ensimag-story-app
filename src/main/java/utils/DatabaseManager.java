package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DatabaseManager {

    private static final Logger LOG = LogManager.getLogger();

    private DatabaseManager() {}

    public static Optional<Connection> getConnection() {
        Connection connection = null;
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context) initContext.lookup("java:/comp/env");

            DataSource dataSource = (DataSource) envContext.lookup("jdbc/story-app");

            connection = dataSource.getConnection();
        } catch (NamingException ex) {
            LOG.error("Failed while getting a JDBC connection. Bad lookup.", ex);
        } catch (SQLException ex) {
            LOG.error("Failed while getting a JDBC connection. Datasource error.", ex);
        }

        return Optional.ofNullable(connection);
    }
}
