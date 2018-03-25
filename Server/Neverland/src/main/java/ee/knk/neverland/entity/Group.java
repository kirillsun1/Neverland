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
//@Table(name = "group")
//public class Group {
//    @Id
//    @GeneratedValue(generator = "increment")
//    @GenericGenerator(name = "increment", strategy = "increment")
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @Column(name = "group_name")
//    @Getter @Setter String name;
//
//    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinColumn(name= "admin_id")
//    @Getter @Setter private User admin;
//
//    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @Getter @Setter private Set<Quest> quests;
//
//    @Column(name = "path")
//    @Getter @Setter private String picturePath;
//
//    @ManyToMany(mappedBy = "subscriptions")
//    @Getter @Setter private Set<User> followers;
//}
//
//