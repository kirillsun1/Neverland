package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.repository.QuestRepository;
import ee.knk.neverland.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestServiceImpl implements QuestService {


    private final QuestRepository questRepository;

    @Autowired
    public QuestServiceImpl(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    @Override
    public Quest addQuest(Quest quest) {
        return questRepository.saveAndFlush(quest);
    }

    @Override
    public List<Quest> getQuests() {
        return questRepository.findAll();
    }
}
