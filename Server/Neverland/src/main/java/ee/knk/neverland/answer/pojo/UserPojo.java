package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPojo implements Pojo {
    @SerializedName("user_name")
    private String username;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("second_name")
    private String secondName;
    @SerializedName("u_id")
    private Long id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("reg_time")
    private String time;
    @SerializedName("followers")
    private int followers;
    @SerializedName("followings")
    private int followings;
    @SerializedName("do_i_follow")
    private boolean iFollow;
    @SerializedName("does_follow_me")
    private boolean followsMe;
}
