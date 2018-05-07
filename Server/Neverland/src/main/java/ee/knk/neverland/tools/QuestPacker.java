package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.answer.pojo.builder.QuestPojoBuilder;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;

import java.util.ArrayList;
import java.util.List;

public class QuestPacker {

    QuestPojoBuilder packQuest(Quest pointer) {
        UserPacker userPacker = new UserPacker();
        UserPojo author = userPacker.packUser(pointer.getUser());
        QuestPojoBuilder builder = new QuestPojoBuilder();
        return builder
                .withId(pointer.getId())
                .withTitle(pointer.getTitle())
                .withDescription(pointer.getDescription())
                .withAuthor(author);
    }

    public List<Pojo> packMyQuests(List<TakenQuest> takenQuests) {
        List<Pojo> packedQuests = new ArrayList<>();
        for (TakenQuest pointer : takenQuests) {
            Quest quest = pointer.getQuest();
            QuestPojoBuilder neededData = packQuest(quest);
            neededData.withAddingTime(pointer.getTimeQuestTaken());
            packedQuests.add(neededData.getQuestPojo());
        }
        return packedQuests;
    }

    public List<Pojo> packAllQuests(List<Quest> quests) {
        List<Pojo> packedQuests = new ArrayList<>();
        for (Quest quest : quests) {
            packedQuests.add(packQuest(quest).getQuestPojo());
        }
        return packedQuests;
    }
}
