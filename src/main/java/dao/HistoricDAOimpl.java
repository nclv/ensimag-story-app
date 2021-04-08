package dao;

import java.sql.Connection;

public class HistoricDAOimpl implements HistoricDAO {
    private Connection connection = null;

    public HistoricDAOimpl(Connection connection) {
        this.connection = connection;
    }
}
