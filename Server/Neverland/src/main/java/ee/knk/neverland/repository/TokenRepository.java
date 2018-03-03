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

    @Query("select token from Token token where token.keyValue = :keyValue")
    Optional<Token> exists(@Param("keyValue") String keyValue);
}
