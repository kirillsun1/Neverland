package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.QuestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QuestServiceImplTest {

    @Mock
    private QuestRepository questRepository;
    @InjectMocks
    private QuestServiceImpl questService;

    @Mock
    private User user;

    @Mock
    private Quest quest;

    @Mock
    private PeopleGroup group;

    @Test
    public void testIfAddQuestCallsRepo() {
        when(questRepository.saveAndFlush(quest)).thenReturn(quest);
        questService.addQuest(quest);
        verify(questRepository).saveAndFlush(quest);
    }

    @Test
    public void testIfGetGroupQuestsCallsRepo() {
        when(questRepository.getGroupQuests(group)).thenReturn(new ArrayList<>());
        questService.getGroupQuests(group);
        verify(questRepository).getGroupQuests(group);
    }

    @Test
    public void testIfGetAuthorsQuestsCallsRepo() {
        when(questRepository.getAuthorsQuests(user)).thenReturn(new ArrayList<>());
        questService.getAuthorsQuests(user);
        verify(questRepository).getAuthorsQuests(user);
    }

    @Test
    public void testIfGetQuestsWithoutCallsRepo() {
        when(questRepository.getQuestsWithoutGroup()).thenReturn(new ArrayList<>());
        questService.getQuestsWithoutGroups();
        verify(questRepository).getQuestsWithoutGroup();
    }

    @Test
    public void testIfGetAllProofsCallsRepo() {
        when(questRepository.getAllQuests()).thenReturn(new ArrayList<>());
        questService.getQuests();
        verify(questRepository).getAllQuests();
    }

}
