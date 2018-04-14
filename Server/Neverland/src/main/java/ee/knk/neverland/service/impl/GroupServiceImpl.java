package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.GroupRepository;
import ee.knk.neverland.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public PeopleGroup addGroup(PeopleGroup peopleGroup) {
        return groupRepository.saveAndFlush(peopleGroup);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.delete(id);
    }

    @Override
    public Optional<PeopleGroup> findGroupById(Long id) {
        return groupRepository.findOneIfExists(id);
    }

    @Override
    public boolean checkAdminRights(Long id, User user) {
        return groupRepository.checkAdminRights(id, user).isPresent();
    }

    @Override
    public void setAvatar(Long groupId, String avatarPath) {
        groupRepository.setAvatar(groupId, avatarPath);
    }

    @Override
    public List<PeopleGroup> getAllGroups() {
        return groupRepository.findAllAndSort();
    }
}
