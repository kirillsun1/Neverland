package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="taken_quests")
public class TakenQuest {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk", nullable = false)
    @Getter @Setter private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "quest_fk", nullable = false)
    @Getter @Setter private Quest quest;

    @Column(name = "taken_time")
    @Getter
    @Setter
    private LocalDateTime timeQuestTaken = LocalDateTime.now();

    public TakenQuest(User user, Quest quest) {
        this.user = user;
        this.quest = quest;
    }
}
