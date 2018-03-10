package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;

public class UserPojo {
    @SerializedName("user_name")
    public String username;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("second_name")
    public String secondName;
}
