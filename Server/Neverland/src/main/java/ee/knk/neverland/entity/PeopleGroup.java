package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "groups")
public class PeopleGroup {

    public PeopleGroup() {

    }

    public PeopleGroup(String name, User admin) {
        this.name = name;
        this.admin = admin;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private Long id;

    @Column(name = "creation_time")
    @Getter private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "group_name", nullable = false)
    @Getter @Setter private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name= "admin", nullable = false)
    @Getter @Setter private User admin;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Quest> quests;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Subscription> subscriptions;

    @Column(name = "avatar")
    @Getter @Setter private String avatar;

    //@Getter @Setter private Set<User> followers;

}

