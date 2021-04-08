package dao;

import java.sql.SQLException;
import java.util.List;

import models.ParentSection;

public interface ParentSectionDAO {
    void saveParentSection(ParentSection parentSection) throws SQLException;
    List<ParentSection> findChildrenParagraphe(long storyId, long parentParagrapheId) throws SQLException;
}
