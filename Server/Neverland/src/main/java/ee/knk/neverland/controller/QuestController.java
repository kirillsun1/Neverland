package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.QuestPojo;
import ee.knk.neverland.answer.QuestsAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.UserPojo;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.tools.QuestPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ee.knk.neverland.constants.Constants;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        questService.addQuest(new Quest(title, description, userPk.get(), groupId));
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value="/getquests")
    public String getQuests(@RequestParam(value="token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        QuestPacker packer = new QuestPacker();
        return gson.toJson(packer.packAllQuests(getQuests()));
    }

    List<Quest> getQuests() {
        return questService.getQuests();
    }


    Quest getQuestById(Long id) {
        return questService.getQuestById(id);
    }

}