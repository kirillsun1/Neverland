package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.Following;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.FollowingService;
import ee.knk.neverland.tools.GroupPacker;
import ee.knk.neverland.tools.UserPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class FollowingController {
    private final FollowingService followingService;
    private final UserController userController;
    private final TokenController tokenController;
    private Gson gson = new Gson();

    @Autowired
    public FollowingController(FollowingService followingService,
                                  UserController userController,
                                  TokenController tokenController) {
        this.followingService = followingService;
        this.userController = userController;
        this.tokenController = tokenController;
    }

    @RequestMapping(value = "/follow")
    public String handleSubscription(@RequestParam(value = "token") String token,
                                     @RequestParam(value = "uid") Long userToFollowId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> toFollow = userController.getUserById(userToFollowId);
        if (!toFollow.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        follow(me.get(), toFollow.get());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    private void follow(User follower, User toFollow) {
        Following following= new Following(toFollow, follower);
        followingService.follow(following);
    }

    @RequestMapping(value = "/unfollow")
    public String unfollow(@RequestParam(value = "token") String token,
                              @RequestParam(value = "uid") Long userToUnfollowId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> userToUnfollow = userController.getUserById(userToUnfollowId);
        if (!userToUnfollow.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        followingService.unfollow(me.get(), userToUnfollow.get());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));
    }

    @RequestMapping(value = "/getMyFollowings")
    public String getMyFollowings(@RequestParam(value = "token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<User> followings = findUsersFollowings(me.get());
        UserPacker packer = new UserPacker();
        return gson.toJson(new ListAnswer(packer.packAllUsers(followings), Constants.SUCCEED));
    }

    @RequestMapping(value = "/getMyFollowers")
    public String getMyFollowers(@RequestParam(value = "token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        UserPacker userPacker = new UserPacker();
        List<User> followers = followingService.getUserFollowers(me.get());
        return gson.toJson(new ListAnswer(userPacker.packAllUsers(followers), Constants.SUCCEED));
    }

    List<User> findUsersFollowings(User user) {
        return followingService.findUsersFollowings(user);
    }


    @RequestMapping(value = "/getUsersFollowings")
    public String getUsersFollowings(@RequestParam(value = "token") String token,
                                 @RequestParam(value = "uid") Long userId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> user = userController.getUserById(userId);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        List<User> followings = findUsersFollowings(user.get());
        UserPacker packer = new UserPacker();
        return gson.toJson(new ListAnswer(packer.packAllUsers(followings), Constants.SUCCEED));
    }

    @RequestMapping(value = "/getUsersFollowers")
    public String getUsersFollowers(@RequestParam(value = "token") String token,
                                      @RequestParam(value = "uid") Long userId) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> user = userController.getUserById(userId);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        UserPacker userPacker = new UserPacker();
        List<User> followers = followingService.getUserFollowers(user.get());
        return gson.toJson(new ListAnswer(userPacker.packAllUsers(followers), Constants.SUCCEED));
    }


    public int getUsersFollowersQuantity(User user) {
        return followingService.getUserFollowersAmount(user);
    }

    public int getUsersFollowingsQuantity(User user) {
        return followingService.getUserFollowingsAmount(user);
    }


    public boolean ifOneFollowsAnother(User follower, User followed) {
        return followingService.ifOneFollowsAnother(follower, followed);
    }
}
