package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.TakenQuestRepository;
import ee.knk.neverland.service.TakenQuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakenQuestServiceImpl implements TakenQuestService {
    private final TakenQuestRepository takenQuestsRepository;

    @Autowired
    public TakenQuestServiceImpl(TakenQuestRepository questRepository) {
        this.takenQuestsRepository = questRepository;
    }

    @Override
    public TakenQuest takeQuest(TakenQuest quest) {
        return takenQuestsRepository.save(quest);
    }

    @Override
    public List<TakenQuest> getQuests(User user) {
        return takenQuestsRepository.getTakenQuestsByUser(user);
    }
}
