package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Following;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.FollowingRepository;
import ee.knk.neverland.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingServiceImpl implements FollowingService {
    private final FollowingRepository followingRepository;

    @Autowired
    public FollowingServiceImpl(FollowingRepository followingRepository) {
        this.followingRepository = followingRepository;
    }
    @Override
    public Following follow(Following following) {
        if (followingRepository.getUsersFollowing(following.getFollower(), following.getUser()).isPresent()) {
            return following;
        }
        return followingRepository.saveAndFlush(following);
    }

    @Override
    public void unfollow(User me, User userToUnfollow) {
        followingRepository.unfollow(me, userToUnfollow);
    }

    @Override
    public List<User> findUsersFollowings(User user) {
        return followingRepository.getUsersFollowings(user);
    }

    @Override
    public List<User> getUserFollowers(User user) {
        return followingRepository.getUserFollowers(user);
    }

    @Override
    public int getUserFollowersAmount(User user) {
        return followingRepository.countUserFollowers(user);
    }

    @Override
    public int getUserFollowingsAmount(User user) {
        return followingRepository.countUserFollowings(user);
    }

}
