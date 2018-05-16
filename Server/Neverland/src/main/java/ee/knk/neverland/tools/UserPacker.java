package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.answer.pojo.builder.UserPojoBuilder;
import ee.knk.neverland.controller.FollowingController;
import ee.knk.neverland.controller.VoteController;
import ee.knk.neverland.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserPacker {

    public UserPojo packDetailedAnotherUser(User user, FollowingController followingController, VoteController voteController, User me) {
        return pack(user)
                .withFollowersAmount(followingController.getUsersFollowersQuantity(user))
                .withFollowingsAmount(followingController.getUsersFollowingsQuantity(user))
                .withIfIFollow(followingController.ifOneFollowsAnother(me, user))
                .withIfFollowsMe(followingController.ifOneFollowsAnother(user, me))
                .withRating(voteController.getUsersRating(user))
                .getUserPojo();
    }

    public UserPojo packDetailedMe(User me, FollowingController followingController, VoteController voteController) {
        return pack(me)
                .withFollowersAmount(followingController.getUsersFollowersQuantity(me))
                .withFollowingsAmount(followingController.getUsersFollowingsQuantity(me))
                .withRating(voteController.getUsersRating(me))
                .getUserPojo();
    }

    private UserPojoBuilder pack(User user) {
        UserPojoBuilder builder = new UserPojoBuilder();
        return builder
                .withId(user.getId())
                .withUsername(user.getUsername())
                .withFirstName(user.getFirstName())
                .withSecondName(user.getSecondName())
                .withAvatar(user.getAvatar())
                .withRegistrationTime(user.getRegisterTime());
    }



    public UserPojo packUser(User user) {
        return pack(user).getUserPojo();
    }

    public List<Pojo> packAllUsers(List<User> users)  {
        List<Pojo> packedUsers = new ArrayList<>();
        for (User user : users) {
            packedUsers.add(packUser(user));
        }
        return packedUsers;
    }
}
