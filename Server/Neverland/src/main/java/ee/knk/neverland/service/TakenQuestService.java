package ee.knk.neverland.service;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface TakenQuestService {
    TakenQuest takeQuest(TakenQuest quest);
    List<TakenQuest> getQuests(User user);
    boolean checkIfQuestIsTaken(User user, Quest quest);
}
