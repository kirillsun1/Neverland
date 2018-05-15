package ee.knk.neverland.controller;

import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.FollowingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @Before
    public void before() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));

    }

    @Test
    public void testIfFollowCallsTokenCheck() {
        followingController.handleSubscription("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfFollowCallsGetUserById() {
        followingController.handleSubscription("token", 0L);
        verify(userController).getUserById(0L);
    }

    @Test
    public void testIfFollowCallsFollow() {
        followingController.handleSubscription("token", 0L);
        verify(followingService).follow(any());
    }

    @Test
    public void testIfUnfollowCallsTokenCheck() {
        followingController.unfollow("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfUnfollowCallsGetUserById() {
        followingController.unfollow("token", 0L);
        verify(userController).getUserById(0L);
    }

    @Test
    public void testIfUnfollowCallsUnfollow() {
        followingController.unfollow("token", 0L);
        verify(followingService).unfollow(any(), any());
    }

    @Test
    public void testIfGetMyFollowingsCallsTokenCheck() {
        followingController.getMyFollowings("token");
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetMyFollowingsCallsService() {
        followingController.getMyFollowings("token");
        verify(followingService).findUsersFollowings(any());
    }

    @Test
    public void testIfGetUsersFollowingsCallsTokenCheck() {
        followingController.getUsersFollowings("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetUsersFollowingsCallsService() {
        followingController.getUsersFollowings("token", 0L);
        verify(followingService).findUsersFollowings(any());
    }

    @Test
    public void testIfGetUsersFollowersQuantityCallsService() {
        followingController.getUsersFollowersQuantity(user);
        verify(followingService).getUserFollowersAmount(user);
    }

    @Test
    public void testIfGetUsersFollowingsAmmountCallsService() {
        followingController.getUsersFollowingsQuantity(user);
        verify(followingService).getUserFollowingsAmount(user);
    }



}
