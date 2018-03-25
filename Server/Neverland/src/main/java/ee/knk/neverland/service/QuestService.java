package ee.knk.neverland.service;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface QuestService {
    Quest addQuest(Quest quest);
    List<Quest> getQuests();
    Quest getQuestById(Long id);

    List<Quest> getAuthorsQuests(User author);
}
