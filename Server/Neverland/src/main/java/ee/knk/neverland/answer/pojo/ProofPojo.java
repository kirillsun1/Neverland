package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProofPojo implements Pojo {
    private Long id;
    private String comment;
    @SerializedName("pic_path")
    private String picturePath;
    private QuestPojo quest;
    private UserPojo proofer;
    @SerializedName("add_time")
    private LocalDateTime time;
    @SerializedName("rating")
    private RatingPojo rating;
    @SerializedName("my_vote")
    private int myVote;
}
