package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    @Query("select quest from Quest quest where quest.user = :author order by quest.id desc")
    List<Quest> getAuthorsQuests(@Param("author") User author);

    @Query("select quest from Quest quest order by quest.id desc")
    List<Quest> getAllQuests();
    //@Query("select takenQuest from TakenQuest takenQuest where takenQuest.user = :user")
    //List<Quest> getTakenQuestsByUser(User user);
}
