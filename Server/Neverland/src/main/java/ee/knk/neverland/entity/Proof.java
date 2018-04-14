package ee.knk.neverland.entity;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "proofs")
@Data
@NoArgsConstructor
public class Proof {

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
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "quest_fk", nullable = false)
    private Quest quest;

    @Column(name = "path", unique = true)
    private String picturePath;

    @Column(name = "comment")
    private String comment;

    @Column(name = "add_time")
    private LocalDateTime time = LocalDateTime.now();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Vote> votes;
}
