package ee.knk.neverland.service;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    PeopleGroup addGroup(PeopleGroup peopleGroup);
    void deleteGroup(Long id);
    Optional<PeopleGroup> findGroupById(Long id);
    boolean checkAdminRights(Long id, User user);

    void setAvatar(Long groupId, String avatarPath);

    List<PeopleGroup> getAllGroups();
}
