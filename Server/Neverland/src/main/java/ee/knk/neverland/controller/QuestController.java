package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.QuestPojo;
import ee.knk.neverland.answer.QuestsAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.UserPojo;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ee.knk.neverland.constants.RegistrationLoginConstants;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class QuestController {
    private final QuestService questService;
    private final TokenController tokenController;
    private Gson gson = new Gson();

    @Autowired
    public QuestController(QuestService questService, TokenService tokenService) {
        this.questService = questService;
        this.tokenController = new TokenController(tokenService);
    }

    @RequestMapping(value="/submitquest")
    public String submitQuest(@RequestParam(value="token") String token, @RequestParam(value = "title") String title, @RequestParam(value = "desc") String description, @RequestParam(value = "gid") Long groupId) {
        Optional<User> userPk = tokenController.getTokenUser(token);
        if (!userPk.isPresent()) {
            return gson.toJson(new StandardAnswer(RegistrationLoginConstants.FAILED));
        }
        questService.addQuest(new Quest(title, description, userPk.get(), groupId));
        return gson.toJson(new StandardAnswer(RegistrationLoginConstants.SUCCEED));
    }

    @RequestMapping(value="/getquests")
    public String getQuests(@RequestParam(value="token") String token) {
        Optional<User> userPk = tokenController.getTokenUser(token);
        if (!userPk.isPresent()) {
            return gson.toJson(new StandardAnswer(RegistrationLoginConstants.FAILED));
        }
        List<Quest> quests = questService.getQuests();
        return gson.toJson(quests);
    }

    private QuestsAnswer getNeededInfoAboutQuests(List<Quest> information) {
        QuestsAnswer answer = new QuestsAnswer();
        for (Quest quest : information) {
            UserPojo user = new UserPojo();
            user.username = quest.getUser().getUsername();
            user.firstName = quest.getUser().getFirstName();
            user.secondName = quest.getUser().getSecondName();
            QuestPojo neededData = new QuestPojo();
            neededData.addingTime = quest.getTime();
            neededData.title = quest.getTitle();
            neededData.description = quest.getDescription();
            neededData.userInformation = user;
            answer.quests.add(neededData);
        }
        return answer;
    }



}
