package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DAOManager {
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
    public Optional<Object> executeAndClose(DAOCommand command) {
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
        return Optional.ofNullable(returnValue);
    }

    /**
     * 
     * @param command
     * @return empty Optional if there is a query error
     */
    public Optional<Object> transaction(DAOCommand command) {
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

        return Optional.ofNullable(returnValue);
    }

    public Optional<Object> transactionAndClose(DAOCommand command) {
        return executeAndClose((daoFactory) -> daoFactory.transaction(command));
    }
}
