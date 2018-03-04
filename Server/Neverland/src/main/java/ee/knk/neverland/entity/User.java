package ee.knk.neverland.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    public User() {
    }

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
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "reg_time")
    private LocalDateTime registerTime = LocalDateTime.now();

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name", nullable = false)
    private String secondName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Token> tokens;

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
