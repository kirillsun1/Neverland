package ee.knk.neverland.repository;

import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.username = :username AND user.password = :password")
    Optional<User> passwordMatches(@Param("username") String username, @Param("password") String password);

    @Query("SELECT user FROM User user WHERE user.username = :username OR user.email = :email")
    Optional<User> exists(@Param("username") String username, @Param("email") String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.avatar = :avatarPath WHERE user.id = :id")
    void setAvatar(@Param("avatarPath") String avatarPath, @Param("id") Long id);
}
