package ee.knk.neverland.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "key")
public class Key {

    public Key(User user, String keyValue) {
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
    private final User user;

    @Column(name = "key_value", nullable = false)
    private final String keyValue;

    public User getUser() {
        return user;
    }

    public String getKeyValue() {
        return keyValue;
    }
}
