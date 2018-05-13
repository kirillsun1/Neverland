package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.TakenQuestRepository;
import ee.knk.neverland.service.TakenQuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TakenQuestServiceImpl implements TakenQuestService {
    private final TakenQuestRepository takenQuestsRepository;

    @Autowired
    public TakenQuestServiceImpl(TakenQuestRepository questRepository) {
        this.takenQuestsRepository = questRepository;
    }

    @Override
    public TakenQuest takeQuest(TakenQuest quest) {
        if(takenQuestsRepository.getIfExists(quest.getQuest(), quest.getUser()).isPresent()) {
            return quest;
        }
        return takenQuestsRepository.saveAndFlush(quest);
    }

    @Override
    public List<Quest> getAllQuestsUserTook(User user) {
        List<TakenQuest> takenQuests = takenQuestsRepository.getTakenQuestsByUser(user);
        List<Quest> quests = new ArrayList<>();
        for (TakenQuest takenQuest : takenQuests) {
            quests.add(takenQuest.getQuest());
        }
        return quests;
    }

    @Override
    public List<TakenQuest> getActiveQuestsUserTook(User user) {
        return takenQuestsRepository.getMyTakenQuests(user);
    }

    @Override
    public boolean checkIfQuestIsTaken(User user, Quest quest) {
        return takenQuestsRepository.getQuestWithUser(user, quest).isPresent();
    }

    @Override
    public Optional<TakenQuest> getQuestTakenByUser(User user, Quest quest) {
        return takenQuestsRepository.getQuestWithUser(user, quest);
    }

    @Override
    public void archive(Long takenQuestId) {
        takenQuestsRepository.archive(takenQuestId);
    }

    @Override
    public void delete(Long takenQuestId) {
        takenQuestsRepository.delete(takenQuestId);
    }

}
