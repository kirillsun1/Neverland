package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentPojo implements Pojo {
    private Long id;
    @SerializedName("text")
    private String text;
    @SerializedName("author")
    private UserPojo author;
    @SerializedName("creation_time")
    private LocalDateTime createTime;

}
