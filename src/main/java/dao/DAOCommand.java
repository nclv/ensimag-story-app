package dao;

import java.sql.SQLException;

public interface DAOCommand {
    /* Must return non null value if there is no error */
    public Object execute(DAOManager daoManager) throws SQLException;
}
