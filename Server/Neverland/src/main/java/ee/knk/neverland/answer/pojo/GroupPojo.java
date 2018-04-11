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
    private LocalDateTime createTime;

    public static class GroupPojoBuilder {
        private GroupPojo groupPojo = new GroupPojo();

        public GroupPojoBuilder setId(Long id) {
            groupPojo.id = id;
            return this;
        }

        public GroupPojoBuilder setName(String name) {
            groupPojo.name = name;
            return this;
        }

        public GroupPojoBuilder setAdmin(UserPojo admin) {
            groupPojo.admin = admin;
            return this;
        }

        public GroupPojoBuilder setAvatar(String avatar) {
            groupPojo.avatar = avatar;
            return this;
        }

        public GroupPojoBuilder setQuantity(int quantity) {
            groupPojo.quantity = quantity;
            return this;
        }

        public GroupPojoBuilder setTime(LocalDateTime time) {
            groupPojo.createTime = time;
            return this;
        }

        public GroupPojo getGroupPojo() {
            return groupPojo;
        }
    }
}
