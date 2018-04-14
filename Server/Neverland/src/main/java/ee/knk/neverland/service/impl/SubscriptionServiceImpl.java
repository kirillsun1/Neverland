package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Subscription;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.SubscriptionRepository;
import ee.knk.neverland.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription subscribe(Subscription subscription) {
        return subscriptionRepository.saveAndFlush(subscription);
    }

    @Override
    public void unsubscribe(User user, PeopleGroup peopleGroup) {
        subscriptionRepository.unsubscribe(user, peopleGroup);
    }

    @Override
    public List<PeopleGroup> getUserGroups(User user) {
        return subscriptionRepository.getUsersGroups(user);
    }

    @Override
    public List<User> getGroupSubscribers(PeopleGroup peopleGroup) {
        return subscriptionRepository.getGroupSubscribers(peopleGroup);
    }

    @Override
    public int getSubscribersAmount(PeopleGroup group) {
        return subscriptionRepository.countGroupSubscribers(group);
    }
}
