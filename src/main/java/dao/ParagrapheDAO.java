package dao;

import java.util.List;

import models.Paragraphe;

public interface ParagrapheDAO {
    long saveParagraphe(Paragraphe paragraphe);
    Paragraphe findParagraphe(long story_id, long paragraphe_id);
    int updateParagraphe(Paragraphe paragraphe);
    List<Paragraphe> findAllStoryParagraphes(long story_id);
}
