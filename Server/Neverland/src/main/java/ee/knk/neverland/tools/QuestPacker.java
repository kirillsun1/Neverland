package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.answer.pojo.builder.QuestPojoBuilder;
import ee.knk.neverland.controller.QuestController;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TakenQuestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestPacker {

    private final QuestController questController;
    private final User me;

    public QuestPacker(QuestController questController, User me) {
        this.questController = questController;
        this.me = me;
    }
    QuestPojo packQuest(Quest pointer) {
        Optional<TakenQuest> takenQuest = questController.getTakenQuestByUserAndQuest(me, pointer);

        UserPacker userPacker = new UserPacker();
        UserPojo author = userPacker.packUser(pointer.getUser());
        QuestPojoBuilder builder = new QuestPojoBuilder();
        builder.withId(pointer.getId())
                .withTitle(pointer.getTitle())
                .withDescription(pointer.getDescription())
                .withAuthor(author)
                .withAddingTime(pointer.getTime());
        takenQuest.ifPresent(takenQuest1 -> builder.withTakenTime(takenQuest1.getTimeQuestTaken()));
        return builder.getQuestPojo();
    }

    public List<Pojo> packMyQuests(List<TakenQuest> takenQuests) {
        List<Pojo> packedQuests = new ArrayList<>();
        for (TakenQuest pointer : takenQuests) {
            Quest quest = pointer.getQuest();
            packedQuests.add(packQuest(quest));
        }
        return packedQuests;
    }

    public List<Pojo> packAllQuests(List<Quest> quests) {
        List<Pojo> packedQuests = new ArrayList<>();
        for (Quest quest : quests) {
            packedQuests.add(packQuest(quest));
        }
        return packedQuests;
    }

    public List<Pojo> packNonPrivateQuests(List<Quest> quests) {
        List<Pojo> packedQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if(quest.getPeopleGroup() == null) {
                packedQuests.add(packQuest(quest));
            }
        }
        return packedQuests;
    }
}
