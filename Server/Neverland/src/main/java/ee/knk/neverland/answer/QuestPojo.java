package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class QuestPojo {
    public Long id;
    public String title;
    @SerializedName("desc")
    public String description;
    @SerializedName("author")
    public UserPojo userInformation;
    @SerializedName("time_created")
    public LocalDateTime addingTime;
    @SerializedName("time_taken")
    public LocalDateTime takenTime;
}
