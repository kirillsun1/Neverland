package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Following;
import ee.knk.neverland.entity.User;
import jdk.nashorn.internal.ir.Optimistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Following following WHERE following.follower = :me AND following.user = :userToUnfollow")
    void unfollow(@Param("me") User user, @Param("userToUnfollow") User userToUnfollow);

    @Query("SELECT following.user FROM Following following WHERE following.follower = :follower ORDER BY following.id ASC")
    List<User> getUsersFollowings(@Param("follower")User follower);

    @Query("SELECT following FROM Following following WHERE following.follower = :follower AND following.user = :user")
    Optional<Following> getUsersFollowing(@Param("follower")User follower, @Param("user") User user);

    @Query("SELECT following.follower FROM Following following WHERE following.user = :user ORDER BY following.id ASC")
    List<User> getUserFollowers(@Param("user")User user);

    @Query("SELECT Count(ALL following) FROM Following following WHERE following.user = :user")
    int countUserFollowers(@Param("user") User user);

    @Query("SELECT Count(ALL following) FROM Following following WHERE following.follower = :follower")
    int countUserFollowings(@Param("follower") User follower);

    @Query("SELECT following FROM Following following WHERE following.follower = :follower AND following.user = :followed")
    Optional<Following> getFollowingByUsers(@Param("follower") User follower, @Param("followed") User followed);
}
