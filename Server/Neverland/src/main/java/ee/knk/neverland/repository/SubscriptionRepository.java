package ee.knk.neverland.repository;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Subscription;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository  extends JpaRepository<Subscription, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Subscription subscription WHERE subscription.user = :user AND subscription.peopleGroup = :peopleGroup")
    void unsubscribe(@Param("user") User user, @Param("peopleGroup") PeopleGroup peopleGroup);

    @Query("SELECT subscription.peopleGroup FROM Subscription subscription WHERE subscription.user = :user ORDER BY subscription.id ASC")
    List<PeopleGroup> getUsersGroups(@Param("user")User user);

    @Query("SELECT subscription.user FROM Subscription subscription WHERE subscription.peopleGroup = :peopleGroup ORDER BY subscription.id ASC")
    List<User> getGroupSubscribers(@Param("peopleGroup")PeopleGroup peopleGroup);

    @Query("SELECT Count(ALL subsription) FROM Subscription subsription WHERE subsription.peopleGroup = :peopleGroup")
    int countGroupSubscribers(@Param("peopleGroup") PeopleGroup peopleGroup);

    @Query("SELECT subscription FROM Subscription subscription WHERE subscription.user = :user AND subscription.peopleGroup = :peopleGroup")
    Optional<Object> getUsersSubscription(@Param("user") User user, @Param("peopleGroup") PeopleGroup peopleGroup);

    @Query("SELECT subscription FROM Subscription subscription WHERE subscription.user = :user AND subscription.peopleGroup = :peopleGroup")
    Optional<Subscription> getUsersSubscriptionToGroup(@Param("user") User user, @Param("peopleGroup") PeopleGroup group);
}
