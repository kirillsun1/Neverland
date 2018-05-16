package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.*;
import ee.knk.neverland.service.GroupService;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.SubscriptionService;
import ee.knk.neverland.service.TakenQuestService;
import ee.knk.neverland.tools.GroupPacker;
import ee.knk.neverland.tools.UserPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class SubscriptionController {

    private final GroupService groupService;
    private final SubscriptionService subscriptionService;
    private final TokenController tokenController;
    private final Gson gson = new Gson();
    private final UserController userController;
    private final TakenQuestService takenQuestService;

    @Autowired
    public SubscriptionController(GroupService groupService,
                                  SubscriptionService subscriptionService,
                                  UserController userController,
                                  TokenController tokenController,
                                  TakenQuestService takenQuestService) {
        this.groupService = groupService;
        this.subscriptionService = subscriptionService;
        this.userController = userController;
        this.tokenController = tokenController;
        this.takenQuestService = takenQuestService;
    }

    @RequestMapping(value = "/subscribe")
    public String handleSubscription(@RequestParam(value = "token") String token,
                                     @RequestParam(value = "gid") Long groupId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<PeopleGroup> group = findGroupById(groupId);
        if (!group.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        subscribe(user.get(), group.get());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    void subscribe(User subscriber, PeopleGroup group) {
        Subscription subscription = new Subscription(subscriber, group);
        subscriptionService.subscribe(subscription);
    }

    @RequestMapping(value = "/unsubscribe")
    public String unsubscribe(@RequestParam(value = "token") String token,
                              @RequestParam(value = "gid") Long groupId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<PeopleGroup> group = findGroupById(groupId);
        if (!group.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        if (groupService.checkAdminRights(groupId, user.get())) {
            return gson.toJson(new StandardAnswer(Constants.PERMISSION_DENIED));
        }
        dropAllQuestsFromGroup(group.get(), user.get());
        subscriptionService.unsubscribe(user.get(), group.get());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value = "/getMyGroups")
    public String getMyGroups(@RequestParam(value = "token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<PeopleGroup> groups = findUsersGroups(me.get());
        GroupPacker packer = new GroupPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllGroups(groups), Constants.SUCCEED));
    }

    @RequestMapping(value = "/getUsersGroups")
    public String getUsersGroups(@RequestParam(value = "token") String token,
                                 @RequestParam(value = "uid") Long userId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> user = userController.getUserById(userId);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        List<PeopleGroup> groups = findUsersGroups(user.get());
        GroupPacker packer = new GroupPacker(this, me.get());
        return gson.toJson(new ListAnswer(packer.packAllGroups(groups), Constants.SUCCEED));
    }

    @RequestMapping(value = "/getGroupSubscribers")
    public String getGroupSubscribers(@RequestParam(value = "token") String token,
                                      @RequestParam(value = "gid") Long groupId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<PeopleGroup> group = findGroupById(groupId);
        if (!group.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        UserPacker userPacker = new UserPacker();
        List<User> subscribers = subscriptionService.getGroupSubscribers(group.get());
        return gson.toJson(new ListAnswer(userPacker.packAllUsers(subscribers), Constants.SUCCEED));
    }




    List<PeopleGroup> findUsersGroups(User me) { return subscriptionService.getUserGroups(me); }

    private Optional<PeopleGroup> findGroupById(Long id) {
        return groupService.findGroupById(id);
    }

    public int getGroupQuantity(PeopleGroup pointer) {
        return subscriptionService.getSubscribersAmount(pointer);
    }

    public boolean isUserSubscribed(User me, PeopleGroup group) {
        return subscriptionService.isUserSubscribed(me, group);
    }

    private void dropAllQuestsFromGroup(PeopleGroup group, User user) {
        List<TakenQuest> takenQuests = takenQuestService.getActiveQuestsUserTook(user);
        if (takenQuests.size() > 0) {
            takenQuests.stream().filter(takenQuest -> group.equals(takenQuest.getQuest().getPeopleGroup()))
                    .forEach(takenQuest -> takenQuestService.drop(takenQuest.getQuest().getId()));
        }

    }
}
