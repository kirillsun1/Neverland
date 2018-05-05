//package ee.knk.neverland.entity;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Entity
//@Table
//public class Group {
//    @Id
//    @GeneratedValue(generator = "increment")
//    @GenericGenerator(name = "increment", strategy = "increment")
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @Getter @Setter private Set<Quest> quests;
//}

