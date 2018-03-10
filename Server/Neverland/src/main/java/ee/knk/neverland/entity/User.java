package ee.knk.neverland.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
<<<<<<< HEAD
    public User() {
    }

    public User(String name, String password, String email, String firstName, String secondName) {
        this.email = email;
        this.username = name;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
    }
=======
>>>>>>> master

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter @Setter private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter private String username;

    @Column(name = "password", nullable = false)
    @Getter @Setter private String password;

<<<<<<< HEAD
    @Column(name = "email", nullable = false, unique = true)
    @Getter @Setter private String email;

    @Column(name = "reg_time")
    @Getter private LocalDateTime registerTime = LocalDateTime.now();

    @Column(name = "first_name", nullable = false)
    @Getter @Setter private String firstName;
=======
    @Column(name = "e-mail")
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Key> keys;

    public String getName() {
        return name;
    }
>>>>>>> master

    @Column(name = "second_name", nullable = false)
    @Getter @Setter private String secondName;

<<<<<<< HEAD
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter private Set<Token> tokens;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter private Set<Quest> quests;
=======
    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }
>>>>>>> master
}
