package ee.knk.neverland.controller;

import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.FollowingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FollowingControllerTest {
    @Mock
    private FollowingService followingService;
    @Mock
    private UserController userController;
    @Mock
    private TokenController tokenController;

    @InjectMocks
    private FollowingController followingController;

    @Mock
    private User user;

    @Test
    public void testIfFollowCallsTokenCheck() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.handleSubscription("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfFollowCallsGetUserById() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.handleSubscription("token", 0L);
        verify(userController).getUserById(0L);
    }

    @Test
    public void testIfFollowCallsFollow() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.handleSubscription("token", 0L);
        verify(followingService).follow(any());
    }

    @Test
    public void testIfUnfollowCallsTokenCheck() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.unfollow("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfUnfollowCallsGetUserById() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.unfollow("token", 0L);
        verify(userController).getUserById(0L);
    }

    @Test
    public void testIfUnfollowCallsUnfollow() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.unfollow("token", 0L);
        verify(followingService).unfollow(any(), any());
    }

    @Test
    public void testIfGetMyFollowingsCallsTokenCheck() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.getMyFollowings("token");
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetMyFollowingsCallsService() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.getMyFollowings("token");
        verify(followingService).findUsersFollowings(any());
    }

    @Test
    public void testIfGetUsersFollowingsCallsTokenCheck() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.getUsersFollowings("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetUsersFollowingsCallsService() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.getUsersFollowings("token", 0L);
        verify(followingService).findUsersFollowings(any());
    }

    @Test
    public void testIfGetUsersFollowersQuantityCallsService() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.getUsersFollowersQuantity(user);
        verify(followingService).getUserFollowersAmount(user);
    }

    @Test
    public void testIfGetUsersFollowingsAmmountCallsService() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        followingController.getUsersFollowingsQuantity(user);
        verify(followingService).getUserFollowingsAmount(user);
    }


}
