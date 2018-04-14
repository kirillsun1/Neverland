package ee.knk.neverland.controller;

import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.GroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GroupControllerTest {
    @Mock
    private TokenController tokenController;
    @Mock
    private GroupService groupService;
    @Mock
    private SubscriptionController subscriptionController;
    @InjectMocks
    private GroupController groupController;
    private String token = "dummy";
    @Mock
    private User user;

    @Before
    public void before() {
        when(tokenController.getTokenUser(token)).thenReturn(Optional.of(user));
        when(groupService.findGroupById(0L)).thenReturn(Optional.empty());
        when(user.getId()).thenReturn(0L);
        when(groupService.checkAdminRights(0L,user)).thenReturn(true);
    }
    @Test
    public void addGroupControlsToken() {
        when(tokenController.getTokenUser(token)).thenReturn(Optional.empty());

        groupController.addGroup(token, "dummy_name");
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void deleteGroupControlsToken() {
        groupController.deleteGroup(token, 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void deleteGroupChecksAdminRights() {
        groupController.deleteGroup(token, 0L);
        verify(groupService).checkAdminRights(0L, user);
    }

    @Test
    public void deleteGroupDeletesGroup() {
        groupController.deleteGroup(token, 0L);
        verify(groupService).deleteGroup(0L);
    }

    @Test
    public void findGroupById() {
        groupController.findGroupById(0L);
        verify(groupService).findGroupById(0L);
    }

    @Test
    public void getGroupInfoControlsToken() {
        groupController.getGroupInfo(token, 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getGroupInfoFindsGroup() {
        groupController.getGroupInfo(token, 0L);
        verify(groupService).findGroupById(0L);
    }

    @Test
    public void getAllGroupsControlsToken() {
        groupController.getAllGroups(token);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getAllGroupsAsksForGroups() {
        when(groupService.getAllGroups()).thenReturn(new ArrayList<>());
        groupController.getAllGroups(token);
        verify(groupService).getAllGroups();
    }

    @Test
    public void setAvatar() {
        groupController.setAvatar(0L, "dummy");
        verify(groupService).setAvatar(0L, "dummy");
    }
}