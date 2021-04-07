package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import models.Story;

public interface StoryDAO {
    long saveStory(Story story) throws SQLException;
    Optional<Story> findStory(long id) throws SQLException;
    List<Story> findStories(long userId) throws SQLException;
    List<Story> findAllOpenPublishedStories() throws SQLException;
    List<Story> findAllPublishedStories() throws SQLException;
    void updateStory(Story story) throws SQLException;
}
