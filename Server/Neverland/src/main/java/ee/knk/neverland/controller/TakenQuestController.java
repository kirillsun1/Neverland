package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.QuestPojo;
import ee.knk.neverland.answer.QuestsAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.UserPojo;
import ee.knk.neverland.constants.RegistrationLoginConstants;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TakenQuestService;
import ee.knk.neverland.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TakenQuestController {
    private final TakenQuestService takenQuestsService;
    private final TokenController tokenController;
    private final QuestController questController;
    private Gson gson = new Gson();

    @Autowired
    public TakenQuestController(TakenQuestService takenQuestsService, TokenService tokenService, QuestController questController) {
        this.takenQuestsService = takenQuestsService;
        this.tokenController = new TokenController(tokenService);
        this.questController = questController;
    }

    @RequestMapping(value="/takequest")
    public String takeQuest(@RequestParam(value="token") String token, @RequestParam(value="qid") Long questId) {
        Optional<User> userPk = tokenController.getTokenUser(token);
        if (!userPk.isPresent()) {
            return gson.toJson(new StandardAnswer(RegistrationLoginConstants.FAILED));
        }
        takenQuestsService.takeQuest(new TakenQuest(userPk.get(), questController.getQuestById(questId)));
        return gson.toJson(new StandardAnswer(RegistrationLoginConstants.SUCCEED));
    }

//    @RequestMapping(value="/getmyquests")
//    public String getQuestsTakenByUser(@RequestParam(value = "token") String token) {
//        Optional<User> userPk = tokenController.getTokenUser(token);
//        if (!userPk.isPresent()) {
//            return gson.toJson(new StandardAnswer(RegistrationLoginConstants.FAILED));
//        }
//
//        List<TakenQuest> questPointers = takenQuestsService.getQuests(userPk.get());
//        return gson.toJson(getNeededInfoAboutQuests(questPointers));
//    }

    private QuestsAnswer getNeededInfoAboutQuests(List<TakenQuest> information) {
        QuestsAnswer answer = new QuestsAnswer();
        for (TakenQuest pointer : information) {
            Quest quest = pointer.getQuest();
            UserPojo user = new UserPojo();
            user.username = quest.getUser().getUsername();
            user.firstName = quest.getUser().getFirstName();
            user.secondName = quest.getUser().getSecondName();
            QuestPojo neededData = new QuestPojo();
            neededData.addingTime = quest.getTime();
            neededData.title = quest.getTitle();
            neededData.description = quest.getDescription();
            neededData.userInformation = user;
            neededData.takenTime = pointer.getTimeQuestTaken();
            neededData.id = quest.getId();
            answer.quests.add(neededData);
        }
        return answer;
    }
}
