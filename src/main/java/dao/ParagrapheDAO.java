package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import models.Paragraphe;

public interface ParagrapheDAO {
    long saveParagraphe(Paragraphe paragraphe) throws SQLException;
    Optional<Paragraphe> findParagraphe(long story_id, long paragraphe_id) throws SQLException;
    void updateParagraphe(Paragraphe paragraphe) throws SQLException;
    List<Paragraphe> findAllStoryParagraphes(long story_id) throws SQLException;
    public List<Paragraphe> getChildrenParagraphe(long storyId, long parentParagrapheId) throws SQLException;
}
