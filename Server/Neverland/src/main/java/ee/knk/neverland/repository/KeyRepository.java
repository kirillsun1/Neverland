package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Key;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface KeyRepository extends JpaRepository<Key, Long> {
    @Query("delete from Key key where key.user = :user")
    void clearUpOutOfDateKeys(@Param("user") User userFk);

    @Query("select key from Key key where key.user = :user and key.keyValue = :keyValue")
    boolean exists(@Param("user") User user, @Param("keyValue") String keyValue);
}
