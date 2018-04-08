package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.QuestPojo.QuestPojoBuilder;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;

import java.util.ArrayList;
import java.util.List;

public class QuestPacker {

    QuestPojo packQuest(Quest pointer) {
        UserPacker userPacker = new UserPacker();
        UserPojo author = userPacker.packUser(pointer.getUser());
        QuestPojoBuilder builder = new QuestPojoBuilder();
        return builder
                .setId(pointer.getId())
                .setTitle(pointer.getTitle())
                .setDescription(pointer.getDescription())
                .setAuthor(author)
                .setAddingTime(pointer.getTime())
                .getQuestPojo();
    }

    public List<Pojo> packMyQuests(List<TakenQuest> takenQuests) {
        List<Pojo> packedQuests = new ArrayList<>();
        for (TakenQuest pointer : takenQuests) {
            Quest quest = pointer.getQuest();
            QuestPojo neededData = packQuest(quest);
            neededData.setTakenTime(pointer.getTimeQuestTaken());
            packedQuests.add(neededData);
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
}
