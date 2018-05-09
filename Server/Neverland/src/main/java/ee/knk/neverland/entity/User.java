package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    public User(String name, String password, String email, String firstName, String secondName) {
        this.email = email;
        this.username = name;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter @Setter private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter private String username;

    @Column(name = "password", nullable = false)
    @Getter @Setter private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Getter @Setter private String email;

    @Column(name = "reg_time")
    @Getter private LocalDateTime registerTime = LocalDateTime.now();

    @Column(name = "first_name", nullable = false)
    @Getter @Setter private String firstName;

    @Column(name = "second_name", nullable = false)
    @Getter @Setter private String secondName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter @Getter private Set<Token> tokens;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Quest> quests;

    @Column(name="avatar")
    @Getter @Setter private String avatar;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<PeopleGroup> adminPeopleGroups;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Subscription> subscriptions;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Vote> votes;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Following> followings;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Following> followers;
}
