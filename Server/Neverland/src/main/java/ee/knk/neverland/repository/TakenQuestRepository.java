package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TakenQuestRepository extends JpaRepository<TakenQuest, Long> {
    @Query("SELECT takenQuest FROM TakenQuest takenQuest WHERE takenQuest.user = :user ORDER BY takenQuest.id DESC")
    List<TakenQuest> getTakenQuestsByUser(@Param("user") User user);

    @Query("SELECT takenQuest FROM TakenQuest takenQuest WHERE takenQuest.user = :user AND takenQuest.active = true ORDER BY takenQuest.id DESC")
    List<TakenQuest> getMyTakenQuests(@Param("user") User user);

    @Query("SELECT takenQuest FROM TakenQuest takenQuest WHERE takenQuest.user = :user AND takenQuest.quest = :quest AND takenQuest.active = true")
    Optional<TakenQuest> getQuestWithUser(@Param("user") User user, @Param("quest") Quest quest);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE TakenQuest takenQuest SET takenQuest.active = false WHERE takenQuest.id = :id")
    void archive(@Param("id") Long id);

    @Query("SELECT takenQuest FROM TakenQuest takenQuest WHERE takenQuest.quest = :quest AND takenQuest.user = :user")
    Optional<TakenQuest> getIfExists(@Param("quest") Quest quest, @Param("user") User user);
}
