package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TakenQuestService;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.tools.QuestPacker;
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

    @RequestMapping(value="/takeQuest")
    public String takeQuest(@RequestParam(value="token") String token,
                            @RequestParam(value="qid") Long questId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Quest quest = questController.getQuestById(questId);
        if (takenQuestsService.checkIfQuestIsTaken(user.get(), quest)) {
            return gson.toJson(new StandardAnswer(Constants.QUEST_IS_TAKEN));
        }
        takenQuestsService.takeQuest(new TakenQuest(user.get(), quest));
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value="/getMyQuests")
    public String getQuestsTakenByUser(@RequestParam(value = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }

        List<TakenQuest> questPointers = takenQuestsService.getActiveQuestsUserTook(user.get());
        QuestPacker packer = new QuestPacker();
        return gson.toJson(new ListAnswer(packer.packMyQuests(questPointers)));
    }


    @RequestMapping(value = "/dropQuest")
    public String deleteQuestFromMyQuests(@RequestParam(value="token") String token,
                                          @RequestParam(value="qid")Long questId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Quest quest = questController.getQuestById(questId);
        Optional<TakenQuest> takenQuest = takenQuestsService.getQuestTakenByUser(user.get(), quest);
        if (!takenQuest.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.QUEST_IS_NOT_TAKEN));
        }
        takenQuestsService.delete(takenQuest.get().getId());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value="/getQuestsToTake")
    public String getQuests(@RequestParam(value="token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Quest> quests = questController.getQuests();
        QuestPacker packer = new QuestPacker();
        List<Quest> needed = getNewQuests(quests, user.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(needed)));
    }

    private List<Quest> getNewQuests(List<Quest> allQuests, User user) {
        List<Quest> questsITook = takenQuestsService.getAllQuestsUserTook(user);
        List<Quest> notMyQuests = new ArrayList<>();
        for(Quest quest : allQuests) {
            if (questsITook.contains(quest) ) {
                continue;
            }
            notMyQuests.add(quest);
        }
        return notMyQuests;
    }

    void archiveTakenQuest(Quest quest, User user) {
        Optional<TakenQuest> takenQuest = takenQuestsService.getQuestTakenByUser(user, quest);
        takenQuest.ifPresent(item -> takenQuestsService.archive(item.getId()));
    }

}
