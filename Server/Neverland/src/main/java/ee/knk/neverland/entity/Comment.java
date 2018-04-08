package ee.knk.neverland.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    public Comment() {

    }

    public Comment(User user, Proof proof, String text) {
        this.author = user;
        this.proof = proof;
        this.text = text;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "author_fk", nullable = false)
    @Getter @Setter private User author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "proof_fk", nullable = false)
    @Getter @Setter private Proof proof;

    @Column(name = "text", nullable = false)
    @Getter @Setter private String text;

    @Column(name = "create_time")
    @Getter @Setter private LocalDateTime createTime = LocalDateTime.now();

}
