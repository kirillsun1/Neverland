package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.TakenQuestService;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.tools.QuestPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ee.knk.neverland.constants.Constants;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class QuestController {
    private final QuestService questService;
    private final TokenController tokenController;
    private final UserController userController;
    private final GroupController groupController;
    private final SubscriptionController subscriptionController;
    private final FollowingController followingController;
    private final TakenQuestService takenQuestService;
    private Gson gson = new Gson();

    @Autowired
    public QuestController(QuestService questService,
                           TakenQuestService takenQuestService,
                           TokenController tokenController,
                           UserController userController,
                           GroupController groupController,
                           SubscriptionController subscriptionController,
                           FollowingController followingController) {
        this.questService = questService;
        this.takenQuestService = takenQuestService;
        this.tokenController = tokenController;
        this.userController = userController;
        this.groupController = groupController;
        this.subscriptionController = subscriptionController;
        this.followingController = followingController;
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
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(getQuests())));
    }

    @RequestMapping(value="/getSuggestedQuests")
    public String getAuthorsQuests(@RequestParam(value="token") String token,
                                   @RequestParam(value="uid") Long userId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> author = userController.getUserById(userId);
        if (!author.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(questService.getAuthorsQuests(author.get()))));
    }

    @RequestMapping(value="/getMySuggestedQuests")
    public String getMySuggestedQuests(@RequestParam(value="token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(questService.getAuthorsQuests(me.get()))));
    }

    @RequestMapping(value="/getGroupQuests")
    public String getGroupQuests(@RequestParam(value="token") String token,
                                 @RequestParam(value = "gid") Long groupId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<PeopleGroup> group = groupController.findGroupById(groupId);
        if (!group.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(questService.getGroupQuests(group.get()))));
    }

    @RequestMapping(value = "/getMyGroupsQuests")
    public String getMyGroupsQuests(@RequestParam(value = "token")String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<PeopleGroup> groups = subscriptionController.findUsersGroups(me.get());
        List<Quest> quests = getQuestsFromGroups(groups);
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(quests)));
    }

    @RequestMapping(value = "/getMyFollowingsQuests")
    public String getMyFollowingsQuests(@RequestParam(value = "token")String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Quest> quests = getQuestsFromUsers(followingController.findUsersFollowings(me.get()));
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllQuests(quests)));
    }


    @RequestMapping(value="/takeQuest")
    public String takeQuest(@RequestParam(value="token") String token,
                            @RequestParam(value="qid") Long questId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Quest quest = getQuestById(questId);
        if (takenQuestService.checkIfQuestIsTaken(user.get(), quest)) {
            return gson.toJson(new StandardAnswer(Constants.QUEST_IS_TAKEN));
        }
        takenQuestService.takeQuest(new TakenQuest(user.get(), quest));
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value="/getMyQuests")
    public String getQuestsTakenByUser(@RequestParam(value = "token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }

        List<TakenQuest> questPointers = takenQuestService.getActiveQuestsUserTook(me.get());
        QuestPacker packer = new QuestPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packMyQuests(questPointers)));
    }


    @RequestMapping(value = "/dropQuest")
    public String deleteQuestFromMyQuests(@RequestParam(value="token") String token,
                                          @RequestParam(value="qid")Long questId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Quest quest = getQuestById(questId);
        Optional<TakenQuest> takenQuest = takenQuestService.getQuestTakenByUser(user.get(), quest);
        if (!takenQuest.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.QUEST_IS_NOT_TAKEN));
        }
        takenQuestService.delete(takenQuest.get().getId());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value="/getQuestsToTake")
    public String getQuestsToTake(@RequestParam(value="token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Quest> allQuests = getQuests();
        QuestPacker packer = new QuestPacker(this, me.get());
        List<Quest> questsITook = takenQuestService.getAllQuestsUserTook(me.get());
        List<Quest> notMyQuests = allQuests.stream().filter(quest -> !questsITook.contains(quest)).collect(Collectors.toList());
        return gson.toJson(new ListAnswer(packer.packAllQuests(notMyQuests)));
    }

    void archiveTakenQuest(Quest quest, User user) {
        Optional<TakenQuest> takenQuest = takenQuestService.getQuestTakenByUser(user, quest);
        takenQuest.ifPresent(item -> takenQuestService.archive(item.getId()));
    }


    List<Quest> getQuestsFromGroups(List<PeopleGroup> groups) {
        List<Quest> quests = new ArrayList<>();
        groups.forEach(group -> quests.addAll(questService.getGroupQuests(group)));
        if (quests.size() > 0) {
            quests.sort(Comparator.comparingLong(Quest::getId));
            Collections.reverse(quests);
        }
        return quests;
    }


    private List<Quest> getQuests() {
        return questService.getQuests();
    }


    Quest getQuestById(Long id) {
        return questService.getQuestById(id);
    }

    private List<Quest> getQuestsFromUsers(List<User> users) {
        List<Quest> quests = new ArrayList<>();
        users.forEach(user -> quests.addAll(questService.getAuthorsQuests(user)));
        if (quests.size() > 0) {
            quests.sort(Comparator.comparingLong(Quest::getId));
            Collections.reverse(quests);
        }
        return quests;
    }

    public Optional<LocalDateTime> getTimeUserTookQuest(User me, Quest quest) {
        return takenQuestService.getTimeUserTookQuest(me, quest);
    }
}
