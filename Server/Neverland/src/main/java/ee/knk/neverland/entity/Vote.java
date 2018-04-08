package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {

    public Vote() {

    }

    public Vote(User user, Proof proof, boolean agreed) {
        this.user = user;
        this.proof = proof;
        this.agreed = agreed;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk", nullable = false)
    @Getter @Setter private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "proof_fk", nullable = false)
    @Getter @Setter private Proof proof;

    @Column(name = "value", nullable = false)
    @Getter @Setter private boolean agreed;


}
