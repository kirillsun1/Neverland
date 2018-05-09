package ee.knk.neverland.service;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;

import java.util.Collection;
import java.util.List;

public interface QuestService {
    Quest addQuest(Quest quest);
    List<Quest> getQuests();
    Quest getQuestById(Long id);

    List<Quest> getGroupQuests(PeopleGroup group);
    List<Quest> getAuthorsQuests(User author);
}
