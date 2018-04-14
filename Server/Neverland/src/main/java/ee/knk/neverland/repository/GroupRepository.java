package ee.knk.neverland.repository;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<PeopleGroup, Long> {
    @Query("SELECT peopleGroup FROM PeopleGroup peopleGroup WHERE peopleGroup.id = :id and peopleGroup.admin = :admin")
    Optional<PeopleGroup> checkAdminRights(@Param("id") Long id, @Param("admin") User user);

    @Query("SELECT peopleGroup FROM PeopleGroup peopleGroup WHERE peopleGroup.id = :id")
    Optional<PeopleGroup> findOneIfExists(@Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PeopleGroup peopleGroup SET peopleGroup.avatar = :avatarPath WHERE peopleGroup.id = :id")
    void setAvatar(@Param("id") Long id, @Param("avatarPath") String avatarPath);

    @Query("SELECT peopleGroup FROM PeopleGroup peopleGroup ORDER BY peopleGroup.id DESC")
    List<PeopleGroup> findAllAndSort();
}
