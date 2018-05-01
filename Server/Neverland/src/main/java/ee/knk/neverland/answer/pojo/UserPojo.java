package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPojo implements Pojo {
    @SerializedName("user_name")
    public String username;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("second_name")
    public String secondName;
    @SerializedName("u_id")
    public Long id;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("reg_time")
    public LocalDateTime time;
}
