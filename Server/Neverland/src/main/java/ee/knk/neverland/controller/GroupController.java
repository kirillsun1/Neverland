package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.Answer;
import ee.knk.neverland.answer.pojo.GroupPojo;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.GroupService;
import ee.knk.neverland.tools.GroupPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class GroupController {

    private final TokenController tokenController;
    private final GroupService groupService;
    private final Gson gson = new Gson();
    private final SubscriptionController subscriptionController;

    @Autowired
    public GroupController(TokenController tokenController, GroupService groupService, SubscriptionController subscriptionController) {
        this.tokenController = tokenController;
        this.groupService = groupService;
        this.subscriptionController = subscriptionController;
    }

    @RequestMapping(value = "/createGroup")
    public String addGroup(@RequestParam(name = "token") String token, @RequestParam(name = "g_name") String groupName) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        PeopleGroup peopleGroup = new PeopleGroup(groupName, user.get());
        groupService.addGroup(peopleGroup);
        return gson.toJson(new Answer(Constants.SUCCEED));
    }

    @RequestMapping(value = "/deleteGroup")
    public String deleteGroup(@RequestParam(name = "token") String token, @RequestParam(name = "gid") Long id) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        if (groupService.checkAdminRights(id, user.get())) {
            groupService.deleteGroup(id);
            return gson.toJson(new Answer(Constants.SUCCEED));
        }
        return gson.toJson(new Answer(Constants.PERMISSION_DENIED));
    }

    Optional<PeopleGroup> findGroupById(Long id) {
        return groupService.findGroupById(id);
    }

    @RequestMapping(value = "/getGroupInfo")
    public String getGroupInfo(@RequestParam(name = "token") String token, @RequestParam(value = "gid") Long groupId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        GroupPacker groupPacker = new GroupPacker(subscriptionController);
        Optional<PeopleGroup> group = groupService.findGroupById(groupId);
        if (!group.isPresent()) {
            return gson.toJson(new Answer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        GroupPojo groupPojo = groupPacker.packGroup(group.get());
        return gson.toJson(new Answer(groupPojo, Constants.SUCCEED));
    }


    @RequestMapping(value = "/getAllGroups")
    public String getAllGroups(@RequestParam(value = "token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        List<PeopleGroup> groups = groupService.getAllGroups();
        GroupPacker packer = new GroupPacker(subscriptionController);
        return gson.toJson(new ListAnswer(packer.packAllGroups(groups), Constants.SUCCEED));
    }

    public void setAvatar(Long groupId, String avatarPath) {
        groupService.setAvatar(groupId, avatarPath);
    }
}
