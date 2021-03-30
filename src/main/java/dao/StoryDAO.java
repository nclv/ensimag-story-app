package dao;

import java.util.List;

import models.Story;

public interface StoryDAO {
    long saveStory(Story story);
    Story findStory(long id);
    List<Story> findStories(long userId);
    List<Story> findAllOpenPublishedStories();
    List<Story> findAllPublishedStories();
    void updateStory(Story story);
}
