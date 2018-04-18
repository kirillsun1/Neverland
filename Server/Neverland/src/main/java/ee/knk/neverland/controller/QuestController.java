package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.tools.QuestPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ee.knk.neverland.constants.Constants;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class QuestController {
    private final QuestService questService;
    private final TokenController tokenController;
    private final UserController userController;
    private final GroupController groupController;
    private Gson gson = new Gson();

    @Autowired
    public QuestController(QuestService questService,
                           TokenController tokenController,
                           UserController userController,
                           GroupController groupController) {
        this.questService = questService;
        this.tokenController = tokenController;
        this.userController = userController;
        this.groupController = groupController;
    }

    @RequestMapping(value="/submitQuest")
    public String submitQuest(@RequestParam(value="token") String token,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "desc") String description,
                              @RequestParam(value = "gid") Long deskId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        if (deskId > 0) {
            Optional<PeopleGroup> peopleGroup = groupController.findGroupById(deskId);
            if (!peopleGroup.isPresent()) {
                return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
            }
            questService.addQuest(new Quest(title, description, user.get(), peopleGroup.get()));
        } else {
            questService.addQuest(new Quest(title, description, user.get()));
        }
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value="/getQuests")
    public String getQuests(@RequestParam(value="token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        QuestPacker packer = new QuestPacker();
        return gson.toJson(new ListAnswer(packer.packAllQuests(getQuests())));
    }

    @RequestMapping(value="/getSuggestedQuests")
    public String getAuthorsQuests(@RequestParam(value="token") String token,
                                   @RequestParam(value="uid") Long userId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        User author = userController.getUserById(userId);
        QuestPacker packer = new QuestPacker();
        return gson.toJson(new ListAnswer(packer.packAllQuests(questService.getAuthorsQuests(author))));
    }

    @RequestMapping(value="/getMySuggestedQuests")
    public String getMySuggestedQuests(@RequestParam(value="token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        QuestPacker packer = new QuestPacker();
        return gson.toJson(new ListAnswer(packer.packAllQuests(questService.getAuthorsQuests(user.get()))));
    }

    @RequestMapping(value="/getGroupQuests")
    public String getGroupQuests(@RequestParam(value="token") String token,
                                 @RequestParam(value = "gid") Long groupId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<PeopleGroup> group = groupController.findGroupById(groupId);
        if (!group.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        QuestPacker packer = new QuestPacker();
        return gson.toJson(new ListAnswer(packer.packAllQuests(questService.getGroupQuests(group.get()))));
    }


    List<Quest> getQuests() {
        return questService.getQuests();
    }


    Quest getQuestById(Long id) {
        return questService.getQuestById(id);
    }

}
