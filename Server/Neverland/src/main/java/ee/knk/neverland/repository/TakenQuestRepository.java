package ee.knk.neverland.repository;

import ee.knk.neverland.entity.TakenQuest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TakenQuestRepository extends JpaRepository<TakenQuest, Long> {
    @Query("select takenQuest from TakenQuest takenQuest where takenQuest.user = :user")
    List<TakenQuest> getTakenQuestsByUser(User user);
}
