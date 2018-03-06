package ee.knk.neverland.service;

import ee.knk.neverland.entity.Quest;

import java.util.List;

public interface QuestService {
    Quest addQuest(Quest quest);
    List<Quest> getQuests();
    Quest getQuestById(Long id);
}
