package ee.knk.neverland.entity;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "proofs")
public class Proof {

    public Proof() {

    }

    public Proof(User user, Quest quest, String picturePath, String comment) {
        this.user = user;
        this.quest = quest;
        this.picturePath = picturePath;
        this.comment = comment;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    @Getter @Setter private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk", nullable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "quest_fk", nullable = false)
    @Getter
    @Setter
    private Quest quest;

    @Column(name = "path", unique = true)
    @Getter @Setter private String picturePath;

    @Column(name = "comment")
    @Getter @Setter private String comment;

    @Column(name = "add_time")
    @Getter @Setter private LocalDateTime time = LocalDateTime.now();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Getter @Setter private Set<Vote> votes;
}
