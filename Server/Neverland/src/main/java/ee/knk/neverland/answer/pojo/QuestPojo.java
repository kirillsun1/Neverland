package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestPojo implements Pojo {
    private Long id;
    private String title;
    @SerializedName("desc")
    private String description;
    @SerializedName("author")
    private UserPojo author;
    @SerializedName("time_created")
    private LocalDateTime addingTime;
    @SerializedName("time_taken")
    private LocalDateTime takenTime;
}
