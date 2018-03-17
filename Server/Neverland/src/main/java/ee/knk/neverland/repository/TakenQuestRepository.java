package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface TakenQuestRepository extends JpaRepository<TakenQuest, Long> {
    @Query("select takenQuest from TakenQuest takenQuest where takenQuest.user = :user")
    List<TakenQuest> getTakenQuestsByUser(@Param("user") User user);

    @Query("select takenQuest from TakenQuest takenQuest where takenQuest.user = :user and takenQuest.quest = :quest and takenQuest.active = true")
    Optional<TakenQuest> getQuestWithUser(@Param("user") User user, @Param("quest") Quest quest);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TakenQuest takenQuest set takenQuest.active = false where takenQuest.id = :id")
    void archive(@Param("id") Long id);
}
