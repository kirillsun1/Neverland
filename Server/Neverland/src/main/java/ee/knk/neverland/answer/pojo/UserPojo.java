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

    public static class UserPojoBuilder {
        private UserPojo userPojo = new UserPojo();

        public UserPojoBuilder setId(Long id) {
            userPojo.id = id;
            return this;
        }

        public UserPojoBuilder setUsername(String username) {
            userPojo.username = username;
            return this;
        }

        public UserPojoBuilder setFirstName(String firstName) {
            userPojo.firstName = firstName;
            return this;
        }

        public UserPojoBuilder setSecondName(String secondName) {
            userPojo.secondName = secondName;
            return this;
        }

        public UserPojoBuilder setAvatar(String avatar) {
            userPojo.avatar = avatar;
            return this;
        }

        public UserPojoBuilder setRegistrationTime(LocalDateTime time) {
            userPojo.time = time;
            return this;
        }

        public UserPojo getUserPojo() {
            return userPojo;
        }
    }
}
