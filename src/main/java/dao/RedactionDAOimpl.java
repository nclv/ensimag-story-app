package dao;

import java.sql.Connection;

public class RedactionDAOimpl implements RedactionDAO {
    private static Connection connection = null;

    public static void setConnection(Connection connection) {
        RedactionDAOimpl.connection = connection;
    }
}
