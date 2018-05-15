package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Subscription;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.SubscriptionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Mock
    private User user;

    @Mock
    private Subscription subscription;

    @Mock
    private PeopleGroup group;

    @Test
    public void testIfSubscribeChecksFollowingRight() {
        when(subscriptionRepository.getUsersSubscription(any(), any())).thenReturn(Optional.empty());
        when(subscriptionRepository.saveAndFlush(any())).thenReturn(subscription);
        subscriptionService.subscribe(subscription);
        verify(subscriptionRepository).getUsersSubscription(any(), any());
    }

    @Test
    public void testIfSubscribeCallsRepo() {
        when(subscriptionRepository.getUsersSubscription(any(), any())).thenReturn(Optional.empty());
        when(subscriptionRepository.saveAndFlush(any())).thenReturn(subscription);
        subscriptionService.subscribe(subscription);
        verify(subscriptionRepository).saveAndFlush(subscription);
    }

    @Test
    public void testIfUnsubscribeCallsRepo() {
        subscriptionService.unsubscribe(user, group);
        verify(subscriptionRepository).unsubscribe(user, group);
    }

    @Test
    public void testIfFindUsersSubscriptionsCallsRepo() {
        when(subscriptionRepository.getUsersGroups(any())).thenReturn(new ArrayList<>());
        subscriptionService.getUserGroups(user);
        verify(subscriptionRepository).getUsersGroups(any());
    }

    @Test
    public void testIfGetGroupSubscribersAmountCallsRepo() {
        when(subscriptionRepository.getGroupSubscribers(any())).thenReturn(new ArrayList<>());
        subscriptionService.getGroupSubscribers(group);
        verify(subscriptionRepository).getGroupSubscribers(group);
    }

    @Test
    public void testIfGetSubscribersAmountReturnsRepoValue() {
        when(subscriptionRepository.countGroupSubscribers(any())).thenReturn(10);
        assertEquals(10, subscriptionService.getSubscribersAmount(group));
    }


    @Test
    public void testIfIIsUserSubscribedReturnsFalseIfRepoGivesEmptyOptional() {
        when(subscriptionRepository.getUsersSubscriptionToGroup(any(), any())).thenReturn(Optional.empty());
        assert(!subscriptionService.isUserSubscribed(user, group));
    }

    @Test
    public void testIfIsUserSubscribedReturnsTrueIfRepoGivesNotEmptyOptional() {
        when(subscriptionRepository.getUsersSubscriptionToGroup(any(), any())).thenReturn(Optional.of(subscription));
        assert(subscriptionService.isUserSubscribed(user, group));
    }

}
