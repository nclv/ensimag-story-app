package dao;

import models.Story;

public interface StoryDAO {
    long saveStory(Story story);
    Story findStory(long id);
}
