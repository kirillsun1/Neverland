package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Following;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.FollowingRepository;
import ee.knk.neverland.repository.GroupRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private GroupServiceImpl groupService;

    @Mock
    private User user;

    @Mock
    private PeopleGroup group;

    @Test
    public void testIfAddGroupByIdCallsRepo() {
        when(groupRepository.saveAndFlush(group)).thenReturn(group);
        groupService.addGroup(group);
        verify(groupRepository).saveAndFlush(group);
    }

    @Test
    public void testIfDeleteGroupCallsRepo() {
        groupService.deleteGroup(0L);
        verify(groupRepository).delete(0L);
    }

    @Test
    public void testIfCheckAdminsRightsReturnsFalseIfRepoGivesEmptyOptional() {
        when(groupRepository.checkAdminRights(0L, user)).thenReturn(Optional.empty());
        assert(!groupService.checkAdminRights(0L, user));
    }

    @Test
    public void testIfCheckAdminsRightsReturnsTrueIfRepoGivesObjectInOptional() {
        when(groupRepository.checkAdminRights(0L, user)).thenReturn(Optional.of(group));
        assert(groupService.checkAdminRights(0L, user));
    }

    @Test
    public void testIfSetAvatarCallsRepo() {
        groupService.setAvatar(0L, "");
        verify(groupRepository).setAvatar(0L, "");
    }

    @Test
    public void testIfGetAllGroupsCallsRepo() {
        when(groupRepository.findAllAndSort()).thenReturn(new ArrayList<>());
        groupService.getAllGroups();
        verify(groupRepository).findAllAndSort();
    }






}