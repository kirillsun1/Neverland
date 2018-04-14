package ee.knk.neverland.service;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Subscription;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface SubscriptionService {

    Subscription subscribe(Subscription subscription);

    void unsubscribe(User user, PeopleGroup peopleGroup);

    List<PeopleGroup> getUserGroups(User user);

    List<User> getGroupSubscribers(PeopleGroup peopleGroup);

    int getSubscribersAmount(PeopleGroup group);
}
