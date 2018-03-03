package ee.knk.neverland.repository;

import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user where user.name = :username and user.password = :password")
    Optional<User> passwordMatches(@Param("username") String username, @Param("password") String password);

    @Query("select user from User user where user.name = :username or user.email = :email")
    Optional<User> exists(@Param("username") String username, @Param("email") String email);

}
