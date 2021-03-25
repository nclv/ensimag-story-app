package dao;

import java.util.List;

import models.Story;

public interface StoryDAO {
    long saveStory(Story story);
    Story findStory(long id);
    List<Story> findAllOpenStories();
    void updateStory(Story story);
}
