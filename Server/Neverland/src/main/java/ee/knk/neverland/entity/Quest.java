package ee.knk.neverland.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "quests")
@Data
@NoArgsConstructor
public class Quest {
    public Quest(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }
    public Quest(String title, String description, User user, PeopleGroup peopleGroup) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.peopleGroup = peopleGroup;
    }

    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 35, nullable = false)
    private String title;

    @Column(name = "description", length = 485, nullable = false)
    private String description;

    @Column(name = "add_time")
    private LocalDateTime time = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "group_fk")
    private PeopleGroup peopleGroup;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Proof> proofs;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<TakenQuest> setOfBeingTaken;
}
