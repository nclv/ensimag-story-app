package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAOManager {

    private static final Logger LOG = LogManager.getLogger();

    private Connection connection = null;

    public DAOManager(Connection connection) {
        this.connection = connection;
    }

    public static DAOManager getInstance(Connection connection) {
        return new DAOManager(connection);
    }

    public UserDAO getUserDAO() {
        return new UserDAOimpl(this.connection);
    }

    public StoryDAO getStoryDAO() {
        return new StoryDAOimpl(this.connection);
    }

    public RedactionDAO getRedactionDAO() {
        return new RedactionDAOimpl(this.connection);
    }

    public ParentSectionDAO getParentSectionDAO() {
        return new ParentSectionDAOimpl(this.connection);
    }

    public ParagrapheDAO getParagrapheDAO() {
        return new ParagrapheDAOimpl(this.connection);
    }

    public InvitedDAO getInvitedDAO() {
        return new InvitedDAOimpl(this.connection);
    }

    public HistoricDAO getHistoricDAO() {
        return new HistoricDAOimpl(this.connection);
    }

    /* Wrappers */

    /**
     * 
     * @param command
     * @return empty Optional if there is a query error
     */
    public Object executeAndClose(DAOCommand command) {
        Object returnValue = null;
        try {
            returnValue = command.execute(this);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    /**
     * 
     * @param command
     * @return empty Optional if there is a query error
     */
    public Object transaction(DAOCommand command) {
        Object returnValue = null;
        try {
            boolean autoCommit = connection.getAutoCommit();
            this.connection.setAutoCommit(false);

            returnValue = command.execute(this);

            this.connection.commit();
            this.connection.setAutoCommit(autoCommit);
        } catch (SQLException rollbackError) {
            rollbackError.printStackTrace();
            returnValue = null; // pour une erreur sur commit ou setAutoCommit
            try {
                connection.rollback();
            } catch (SQLException rbe) {
                rollbackError.printStackTrace();
            }
        }

        return returnValue;
    }

    public Object transactionAndClose(DAOCommand command) {
        return executeAndClose((daoFactory) -> daoFactory.transaction(command));
    }
}
