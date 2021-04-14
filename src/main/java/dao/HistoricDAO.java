package dao;

import java.sql.SQLException;
import java.util.List;

import models.Historic;

public interface HistoricDAO {
    void saveHistoric(Historic historic) throws SQLException;
    void saveHistory(List<Historic> history) throws SQLException;
    List<Historic> findAllHistoric(long userId, long storyId) throws SQLException;
    void removeAllHistoric(long userId, long storyId) throws SQLException;
}
