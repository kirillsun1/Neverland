package ee.knk.neverland.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token {
    public Token() {

    }

    public Token(User user, String keyValue) {
        this.keyValue = keyValue;
        this.user = user;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @Column(name = "key_value", nullable = false, unique = true)
    private String keyValue;

    @Column(name="active")
    private boolean active = true;

    public User getUser() {
        return user;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
