package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;

public class UserPojo {
    public String username;
    @SerializedName("f_name")
    public String firstName;
    @SerializedName("s_name")
    public String secondName;
}
