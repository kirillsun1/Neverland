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

    public User(String name, String password, String email) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "reg_time")
    private LocalDateTime registerTime = LocalDateTime.now();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Token> tokens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
