package ee.knk.neverland.controller;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.GroupService;
import ee.knk.neverland.service.SubscriptionService;
import ee.knk.neverland.service.TakenQuestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubscriptionControllerTest {
    @Mock
    private GroupService groupService;
    @Mock
    private SubscriptionService subscriptionService;
    @Mock
    private UserController userController;
    @Mock
    private TokenController tokenController;
    @Mock
    private TakenQuestService takenQuestService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Mock
    private User user;
    @Mock
    private PeopleGroup peopleGroup;

    @Before
    public void before() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        when(groupService.findGroupById(0L)).thenReturn(Optional.of(peopleGroup));
        when(subscriptionService.getUserGroups(any())).thenReturn(new ArrayList<>());
        when(subscriptionService.getGroupSubscribers(any())).thenReturn(new ArrayList<>());
    }

    @Test
    public void testIfSubscribeCallsTokenCheck() {
        subscriptionController.handleSubscription("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfFollowCallsGetGroupById() {
        subscriptionController.handleSubscription("token", 0L);
        verify(groupService).findGroupById(0L);
    }

    @Test
    public void testIfSubscribeCallsSubscribe() {
        subscriptionController.handleSubscription("token", 0L);
        verify(subscriptionService).subscribe(any());
    }

    @Test
    public void testIfUnsubscribeCallsTokenCheck() {
        subscriptionController.unsubscribe("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfUnsubscribeCallsGetUserById() {
        subscriptionController.unsubscribe("token", 0L);
        verify(groupService).findGroupById(0L);
    }

    @Test
    public void testIfUnsubscribeChecksIfUserIsNotAdmin() {
        subscriptionController.unsubscribe("token", 0L);
        verify(groupService).checkAdminRights(0L, user);
    }


    @Test
    public void testIfUnsubscribeCallsUnsubscribe() {
        subscriptionController.unsubscribe("token", 0L);
        verify(subscriptionService).unsubscribe(any(), any());
    }

    @Test
    public void testIfGetMyGroupsCallsTokenCheck() {
        subscriptionController.getMyGroups("token");
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetMyGroupsCallsService() {
        subscriptionController.getMyGroups("token");
        verify(subscriptionService).getUserGroups(any());
    }

    @Test
    public void testIfGetUsersGroupsCallsTokenCheck() {
        subscriptionController.getUsersGroups("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetUsersGroupsCallsService() {
        subscriptionController.getUsersGroups("token", 0L);
        verify(subscriptionService).getUserGroups(any());
    }


    @Test
    public void testIfGetGroupSubscribersCallsToken() {
        subscriptionController.getGroupSubscribers("token", 0L);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfGetGroupSubscribersCallsService() {
        subscriptionController.getGroupSubscribers("token", 0L);
        verify(subscriptionService).getGroupSubscribers(any());
    }

    @Test
    public void testIfGetGroupQuantityCallsService() {
        subscriptionController.getGroupQuantity(peopleGroup);
        verify(subscriptionService).getSubscribersAmount(any());
    }



}
