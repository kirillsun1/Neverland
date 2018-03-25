package ee.knk.neverland.service;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;

import java.util.List;
import java.util.Optional;

public interface TakenQuestService {
    TakenQuest takeQuest(TakenQuest quest);
    List<TakenQuest> getAllQuestsUserTook(User user);
    boolean checkIfQuestIsTaken(User user, Quest quest);
    Optional<TakenQuest> getQuestTakenByUser(User user, Quest quest);
    List<TakenQuest> getActiveQuestsUserTook(User user);
    void archive(Long takenQuestId);
    void delete(Long takenQuestId);
}
