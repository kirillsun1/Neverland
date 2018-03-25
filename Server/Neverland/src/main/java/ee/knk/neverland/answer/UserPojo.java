package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;
import lombok.Setter;

public class UserPojo {
    @SerializedName("user_name")
    public String username;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("second_name")
    public String secondName;
    @SerializedName("u_id")
    public Long userId;
    @SerializedName("avatar")
    public String avatar;
}
