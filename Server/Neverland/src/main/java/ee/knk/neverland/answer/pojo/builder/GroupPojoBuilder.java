package ee.knk.neverland.answer.pojo.builder;


import ee.knk.neverland.answer.pojo.GroupPojo;
import ee.knk.neverland.answer.pojo.UserPojo;

import java.time.LocalDateTime;

public class GroupPojoBuilder {
    private GroupPojo groupPojo = new GroupPojo();

    public GroupPojoBuilder withId(Long id) {
        groupPojo.setId(id);
        return this;
    }

    public GroupPojoBuilder withName(String name) {
        groupPojo.setName(name);
        return this;
    }

    public GroupPojoBuilder withAdmin(UserPojo admin) {
        groupPojo.setAdmin(admin);
        return this;
    }

    public GroupPojoBuilder withAvatar(String avatar) {
        groupPojo.setAvatar(avatar);
        return this;
    }

    public GroupPojoBuilder withQuantity(int quantity) {
        groupPojo.setQuantity(quantity);
        return this;
    }

    public GroupPojoBuilder withTime(LocalDateTime time) {
        groupPojo.setCreateTime(time);
        return this;
    }

    public GroupPojo getGroupPojo() {
        return groupPojo;
    }
}