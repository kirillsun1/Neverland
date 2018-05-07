package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupPojo implements Pojo {
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("admin")
    private UserPojo admin;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("creation_time")
    private String createTime;

}
