package ee.knk.neverland.service;

import ee.knk.neverland.entity.Following;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface FollowingService {
    Following follow(Following following);

    void unfollow(User me, User userToUnfollow);

    List<User> findUsersFollowings(User user);

    List<User> getUserFollowers(User user);

    int getUserFollowersAmount(User user);

    int getUserFollowingsAmount(User user);
}
