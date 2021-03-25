package dao;

import models.Paragraphe;

public interface ParagrapheDAO {
    long saveParagraphe(Paragraphe paragraphe);
    Paragraphe findParagraphe(long story_id, long paragraphe_id);
    void updateParagraphe(Paragraphe paragraphe);
}
