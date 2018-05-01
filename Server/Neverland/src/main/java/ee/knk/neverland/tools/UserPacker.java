package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.answer.pojo.builder.UserPojoBuilder;
import ee.knk.neverland.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserPacker {

    public UserPojo packUser(User user) {
        UserPojoBuilder builder = new UserPojoBuilder();
        return builder
                .withId(user.getId())
                .withUsername(user.getUsername())
                .withFirstName(user.getFirstName())
                .withSecondName(user.getSecondName())
                .withAvatar(user.getAvatar())
                .withRegistrationTime(user.getRegisterTime())
                .getUserPojo();
    }

    public List<Pojo> packAllUsers(List<User> users)  {
        List<Pojo> packedUsers = new ArrayList<>();
        for (User user : users) {
            packedUsers.add(packUser(user));
        }
        return packedUsers;
    }
}
