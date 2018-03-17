package ee.knk.neverland.service.impl;

import com.sun.xml.internal.bind.v2.TODO;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.TakenQuestRepository;
import ee.knk.neverland.service.TakenQuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return takenQuestsRepository.saveAndFlush(quest);
    }

    @Override
    public List<TakenQuest> getQuests(User user) {
        return takenQuestsRepository.getTakenQuestsByUser(user);
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
}
