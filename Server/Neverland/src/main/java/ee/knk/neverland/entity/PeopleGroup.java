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
@Table(name = "groups")
@Data
@NoArgsConstructor
public class PeopleGroup {

    public PeopleGroup(String name, User admin) {
        this.name = name;
        this.admin = admin;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "creation_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "group_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name= "admin", nullable = false)
    private User admin;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Quest> quests;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Subscription> subscriptions;

    @Column(name = "avatar")
    private String avatar;

    //@Getter @Setter private Set<User> followers;

}

