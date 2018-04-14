package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.GroupPojo;
import ee.knk.neverland.answer.pojo.GroupPojo.GroupPojoBuilder;
import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.UserPojo;

import ee.knk.neverland.controller.SubscriptionController;
import ee.knk.neverland.entity.PeopleGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupPacker {

    private final SubscriptionController subscriptionController;

    public GroupPacker(SubscriptionController subscriptionController) {
        this.subscriptionController = subscriptionController;
    }

    public GroupPojo packGroup(PeopleGroup pointer) {
        UserPacker userPacker = new UserPacker();
        UserPojo admin = userPacker.packUser(pointer.getAdmin());
        GroupPojoBuilder groupPojoBuilder = new GroupPojoBuilder();
        return groupPojoBuilder.setId(pointer.getId())
                .setAdmin(admin)
                .setName(pointer.getName())
                .setAvatar(pointer.getAvatar())
                .setQuantity(subscriptionController.getGroupQuantity(pointer))
                .setTime(pointer.getCreateTime())
                .getGroupPojo();

    }

    public List<Pojo> packAllGroups(List<PeopleGroup> groups)  {
        List<Pojo> packedGroups = new ArrayList<>();
        for (PeopleGroup group : groups) {
            packedGroups.add(packGroup(group));
        }
        return packedGroups;
    }
}