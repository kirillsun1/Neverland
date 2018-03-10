package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
public class Quest {
    public Quest() {

    }

    public Quest(String title, String description, User user, Long group) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter @Setter private Long id;

    @Column(name = "title", length = 35, nullable = false)
    @Getter @Setter private String title;

    @Column(name = "description", length = 485, nullable = false)
    @Getter @Setter private String description;

    @Column(name = "add_time")
    @Getter @Setter private LocalDateTime time = LocalDateTime.now();

//    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinColumn(name = "group_fk")
//    @Getter @Setter private Group group;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk")
    @Getter @Setter private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Solution> solutions;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<TakenQuest> setOfBeingTaken;
}
