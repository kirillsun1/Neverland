package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("DELETE from Token token WHERE token.user = :user")
    void clearUpOutOfDateKeys(@Param("user") User userFk);

    @Query("SELECT token FROM Token token WHERE token.value = :value AND token.active = true")
    Optional<Token> isValid(@Param("value") String value);

    @Query("SELECT token FROM Token token WHERE token.value = :value")
    Optional<Token> exists(@Param("value") String value);

    @Query("SELECT user FROM Token token WHERE token.value = :value")
    Optional<User> getTokenUser(@Param("value") String value);
}
