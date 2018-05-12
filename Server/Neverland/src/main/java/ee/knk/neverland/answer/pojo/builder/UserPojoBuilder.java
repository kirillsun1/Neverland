package ee.knk.neverland.answer.pojo.builder;


import ee.knk.neverland.answer.pojo.UserPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        userPojo.setTime(time.format(formatter));
        return this;
    }

    public UserPojoBuilder withFollowersAmount(int followersAmount) {
        userPojo.setFollowers(followersAmount);
        return this;
    }

    public UserPojoBuilder withFollowingsAmount(int followingsAmount) {
        userPojo.setFollowings(followingsAmount);
        return this;
    }

    public UserPojo getUserPojo() {
        return userPojo;
    }

    public UserPojoBuilder withIfIFollow(boolean ifIFollow) {
        userPojo.setIFollow(ifIFollow);
        return this;
    }

    public UserPojoBuilder withIfFollowsMe(boolean ifFollowsMe) {
        userPojo.setFollowsMe(ifFollowsMe);
        return this;
    }
}