package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@NoArgsConstructor
public class Token {

    public Token(User user, String value) {
        this.value = value;
        this.user = user;
    }

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

    @Column(name="active")
    @Getter @Setter private boolean active = true;

}
