package dao;

import java.sql.SQLException;
import java.util.List;

import models.Story;

public interface StoryDAO {
    long saveStory(Story story) throws SQLException;
    Story findStory(long id) throws SQLException;
    List<Story> findStories(long userId) throws SQLException;
    List<Story> findAllOpenPublishedStories() throws SQLException;
    List<Story> findAllPublishedStories() throws SQLException;
    void updateStory(Story story) throws SQLException;
}
