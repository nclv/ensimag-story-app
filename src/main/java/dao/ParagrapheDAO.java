package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import models.Paragraphe;

public interface ParagrapheDAO {
    long saveParagraphe(Paragraphe paragraphe) throws SQLException;
    Optional<Paragraphe> findParagraphe(long storyId, long paragrapheId) throws SQLException;
    void updateParagraphe(Paragraphe paragraphe) throws SQLException;
    List<Paragraphe> findAllStoryParagraphes(long storyId) throws SQLException;
    void removeParagraphe(Paragraphe paragraphe) throws SQLException;
    List<Paragraphe> findAllParentsFromFinalChild(long storyId) throws SQLException;
}
