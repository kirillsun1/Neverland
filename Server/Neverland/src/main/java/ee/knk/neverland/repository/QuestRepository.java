package ee.knk.neverland.repository;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    @Query("SELECT quest FROM Quest quest WHERE quest.user = :author ORDER BY quest.id ASC")
    List<Quest> getAuthorsQuests(@Param("author") User author);

    @Query("SELECT quest FROM Quest quest ORDER BY quest.id ASC")
    List<Quest> getAllQuests();

    @Query("SELECT quest FROM Quest quest WHERE quest.peopleGroup = :peopleGroup ORDER BY quest.id ASC")
    List<Quest> getGroupQuests(@Param("peopleGroup") PeopleGroup peopleGroup);

    @Query("SELECT quest FROM Quest quest WHERE quest.peopleGroup = null")
    List<Quest> getQuestsWithoutGroup();
}
