package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class ProofPojo {
    public Long id;
    public String comment;
    @SerializedName("pic_path")
    public String picturePath;
    @SerializedName("quest")
    public QuestPojo quest;
    @SerializedName("proofer")
    public UserPojo proofer;
    @SerializedName("add_time")
    public LocalDateTime time;
}
