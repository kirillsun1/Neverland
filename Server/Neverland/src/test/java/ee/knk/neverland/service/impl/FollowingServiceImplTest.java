package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Following;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.FollowingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FollowingServiceImplTest {

    @Mock
    private FollowingRepository followingRepository;
    @InjectMocks
    private FollowingServiceImpl followingService;

    @Mock
    private User user;

    @Mock
    private Following following;

    @Test
    public void testIfFollowChecksFollowingRight() {
        when(followingRepository.getUsersFollowing(any(), any())).thenReturn(Optional.empty());
        when(followingRepository.saveAndFlush(any())).thenReturn(following);
        followingService.follow(following);
        verify(followingRepository).getUsersFollowing(any(), any());
    }

    @Test
    public void testIfFollowCallsRepo() {
        when(followingRepository.getUsersFollowing(any(), any())).thenReturn(Optional.empty());
        when(followingRepository.saveAndFlush(any())).thenReturn(following);
        followingService.follow(following);
        verify(followingRepository).saveAndFlush(following);
    }

    @Test
    public void testIfUnfollowCallsRepo() {
        followingService.unfollow(user, user);
        verify(followingRepository).unfollow(user, user);
    }

    @Test
    public void testIfFindUsersFollowingsCallsRepo() {
        when(followingRepository.getUsersFollowings(any())).thenReturn(new ArrayList<>());
        followingService.findUsersFollowings(user);
        verify(followingRepository).getUsersFollowings(any());
    }

    @Test
    public void testIfGetUsersFollowersAmountCallsRepo() {
        when(followingRepository.getUserFollowers(any())).thenReturn(new ArrayList<>());
        followingService.getUserFollowers(user);
        verify(followingRepository).getUserFollowers(user);
    }

    @Test
    public void testIfGetUsersFollowersAmountReturnsRepoValue() {
        when(followingRepository.countUserFollowers(any())).thenReturn(10);
        assertEquals(10, followingService.getUserFollowersAmount(user));
    }

    @Test
    public void testIfGetUsersFollowingsAmountReturnsRepoValue() {
        when(followingRepository.countUserFollowings(any())).thenReturn(15);
        assertEquals(15, followingService.getUserFollowingsAmount(user));
    }

    @Test
    public void testIfIfOneFollowsAnotherReturnsFalseIfRepoGivesEmptyOptional() {
        when(followingRepository.getFollowingByUsers(any(), any())).thenReturn(Optional.empty());
        assert (!followingService.ifOneFollowsAnother(user, user));
    }

    @Test
    public void testIfIfOneFollowsAnotherReturnsTrueIfRepoGivesNotEmptyOptional() {
        when(followingRepository.getFollowingByUsers(any(), any())).thenReturn(Optional.of(following));
        assert (followingService.ifOneFollowsAnother(user, user));
    }

}
