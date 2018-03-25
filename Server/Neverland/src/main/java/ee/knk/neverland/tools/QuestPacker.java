package ee.knk.neverland.tools;

import ee.knk.neverland.answer.QuestPojo;
import ee.knk.neverland.answer.QuestList;
import ee.knk.neverland.answer.UserPojo;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;

import java.util.List;

public class QuestPacker {

    QuestPojo packQuest(Quest quest) {
        UserPojo user = new UserPojo();
        user.username = quest.getUser().getUsername();
        user.firstName = quest.getUser().getFirstName();
        user.secondName = quest.getUser().getSecondName();
        user.userId = quest.getUser().getId();
        QuestPojo neededData = new QuestPojo();
        neededData.addingTime = quest.getTime();
        neededData.title = quest.getTitle();
        neededData.description = quest.getDescription();
        neededData.userInformation = user;
        neededData.id = quest.getId();
        return neededData;
    }

    public QuestList packMyQuests(List<TakenQuest> information) {
        QuestList packedQuests = new QuestList();
        for (TakenQuest pointer : information) {
            Quest quest = pointer.getQuest();
            QuestPojo neededData = packQuest(quest);
            neededData.takenTime = pointer.getTimeQuestTaken();
            packedQuests.quests.add(neededData);
        }
        return packedQuests;
    }

    public QuestList packAllQuests(List<Quest> information) {
        QuestList answer = new QuestList();
        for (Quest quest : information) {
            answer.quests.add(packQuest(quest));
        }
        return answer;
    }
}
