package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class QuestPojo {
    public String title;
    @SerializedName("desc")
    public String description;
    @SerializedName("user")
    public UserPojo userInformation;
    @SerializedName("time")
    public LocalDateTime addingTime;
}
