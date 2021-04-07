package dao;

import java.sql.Connection;

public class RedactionDAOimpl implements RedactionDAO {
    private Connection connection = null;

    public RedactionDAOimpl(Connection connection) {
        this.connection = connection;
    }
}
