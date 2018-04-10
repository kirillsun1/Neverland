package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("delete from Token token where token.user = :user")
    void clearUpOutOfDateKeys(@Param("user") User userFk);

    @Query("select token from Token token where token.value = :value and token.active = true")
    Optional<Token> isValid(@Param("value") String value);

    @Query("select token from Token token where token.value = :value")
    Optional<Token> exists(@Param("value") String value);

    @Query("select user from Token token where token.value = :value")
    Optional<User> getTokenUser(@Param("value") String value);
}
