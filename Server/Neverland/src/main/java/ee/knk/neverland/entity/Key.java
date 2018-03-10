package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
<<<<<<< HEAD:Server/Neverland/src/main/java/ee/knk/neverland/entity/Token.java
@Table(name = "tokens")
public class Token {
    public Token() {

    }

    public Token(User user, String value) {
        this.value = value;
        this.user = user;
    }

=======
@Table(name = "key")
public class Key {
>>>>>>> master:Server/Neverland/src/main/java/ee/knk/neverland/entity/Key.java
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk", nullable = false)
    @Getter @Setter private User user;

    @Column(name = "value", nullable = false, unique = true)
    @Getter @Setter private String value;

<<<<<<< HEAD:Server/Neverland/src/main/java/ee/knk/neverland/entity/Token.java
    @Column(name="active")
    @Getter @Setter private boolean active = true;

=======
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
>>>>>>> master:Server/Neverland/src/main/java/ee/knk/neverland/entity/Key.java
}
