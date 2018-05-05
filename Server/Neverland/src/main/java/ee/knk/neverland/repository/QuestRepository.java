package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    //@Query("select takenQuest from TakenQuest takenQuest where takenQuest.user = :user")
    //List<Quest> getTakenQuestsByUser(User user);
}
