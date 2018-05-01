package ee.knk.neverland.answer.pojo.builder;


import ee.knk.neverland.answer.pojo.UserPojo;

import java.time.LocalDateTime;

public class UserPojoBuilder {
    private UserPojo userPojo = new UserPojo();

    public UserPojoBuilder withId(Long id) {
        userPojo.setId(id);
        return this;
    }

    public UserPojoBuilder withUsername(String username) {
        userPojo.setUsername(username);
        return this;
    }

    public UserPojoBuilder withFirstName(String firstName) {
        userPojo.setFirstName(firstName);
        return this;
    }

    public UserPojoBuilder withSecondName(String secondName) {
        userPojo.setSecondName(secondName);
        return this;
    }

    public UserPojoBuilder withAvatar(String avatar) {
        userPojo.setAvatar(avatar);
        return this;
    }

    public UserPojoBuilder withRegistrationTime(LocalDateTime time) {
        userPojo.setTime(time);
        return this;
    }

    public UserPojo getUserPojo() {
        return userPojo;
    }
}