package ee.knk.neverland.tools;

import ee.knk.neverland.answer.QuestPojo;
import ee.knk.neverland.answer.QuestsAnswer;
import ee.knk.neverland.answer.UserPojo;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;

import java.util.List;

public class QuestPacker {

    private QuestPojo packQuest(Quest quest) {
        UserPojo user = new UserPojo();
        user.username = quest.getUser().getUsername();
        user.firstName = quest.getUser().getFirstName();
        user.secondName = quest.getUser().getSecondName();
        QuestPojo neededData = new QuestPojo();
        neededData.addingTime = quest.getTime();
        neededData.title = quest.getTitle();
        neededData.description = quest.getDescription();
        neededData.userInformation = user;
        neededData.id = quest.getId();
        return neededData;
    }

    public QuestsAnswer packMyQuests(List<TakenQuest> information) {
        QuestsAnswer answer = new QuestsAnswer();
        for (TakenQuest pointer : information) {
            Quest quest = pointer.getQuest();
            QuestPojo neededData = packQuest(quest);
            neededData.takenTime = pointer.getTimeQuestTaken();
            answer.quests.add(neededData);
        }
        return answer;
    }

    public QuestsAnswer packAllQuests(List<Quest> information) {
        QuestsAnswer answer = new QuestsAnswer();
        for (Quest quest : information) {
            answer.quests.add(packQuest(quest));
        }
        return answer;
    }
}
