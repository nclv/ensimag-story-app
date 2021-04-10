package dao;

import java.sql.SQLException;
import java.util.List;

import models.Historic;

public interface HistoricDAO {
    long saveHistoric(Historic historic) throws SQLException;
    List<Historic> findAllHistoric(long userId, long storyId) throws SQLException;
}
