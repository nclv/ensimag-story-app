package dao;

import java.sql.SQLException;

import models.ParentSection;

public interface ParentSectionDAO {
    void saveParentSection(ParentSection parentSection) throws SQLException;
}
