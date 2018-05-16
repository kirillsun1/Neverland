package ee.knk.neverland.controller;

import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QuestControllerTest {

    @Mock
    private QuestService questService;
    @Mock
    private TokenController tokenController;
    @Mock
    private UserController userController;
    @Mock
    private GroupController groupController;
    @InjectMocks
    private QuestController questController;

    @Mock
    private User user;

    private String token = "dummy";

    @Before
    public void before() {
        when(tokenController.getTokenUser(token)).thenReturn(Optional.of(user));
        when(groupController.findGroupById(1L)).thenReturn(Optional.empty());
        when(groupController.findGroupById(0L)).thenReturn(Optional.empty());
        when(userController.getUserById(0L)).thenReturn(Optional.empty());
    }

    @Test
    public void submitQuestControlsToken() {
        questController.submitQuest(token, "dummy", "dummy", 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void submitQuestFindsGroup() {
        questController.submitQuest(token, "dummy", "dummy", 1L);
        verify(groupController).findGroupById(1L);
    }

    @Test
    public void getQuestsControlsToken() {
        questController.getQuests(token);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getQuestsAsksForQuests() {
        questController.getQuests(token);
        verify(questService).getQuestsWithoutGroups();
    }

    @Test
    public void getAuthorsQuestsControlsToken() {
        questController.getAuthorsQuests(token, 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getAuthorsQuestsAsksForUser() {
        questController.getAuthorsQuests(token, 0L);
        verify(userController).getUserById(0L);
    }

    @Test
    public void getMySuggestedQuestsControlsToken() {
        questController.getMySuggestedQuests(token);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getMySuggestedQuestsAsksForQuests() {
        questController.getMySuggestedQuests(token);
        verify(questService).getAuthorsQuests(user);
    }

    @Test
    public void getGroupQuestsControlsToken() {
        questController.getGroupQuests(token, 0L);
        verify(tokenController).getTokenUser(token);
    }
}