package ee.knk.neverland.controller;

import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.ProofService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProofControllerTest {
    @Mock
    private TokenController tokenController;
    @Mock
    private ProofService proofService;
    @Mock
    private QuestController questController;
    @Mock
    private UserController userController;
    @Mock
    private VoteController voteController;
    @InjectMocks
    private ProofController proofController;

    @Mock
    private User user;
    @Mock
    private Quest quest;
    private String token = "dummy";

    @Before
    public void before() {
        when(questController.getQuestById(0L)).thenReturn(quest);
        when(tokenController.getTokenUser(token)).thenReturn(Optional.of(user));
        when(proofService.getUsersProofs(user)).thenReturn(new ArrayList<>());
        when(userController.getUserById(0L)).thenReturn(Optional.of(user));
        when(proofService.getProofById(0L)).thenReturn(Optional.empty());
    }


    @Test
    public void addProofAsksForQuest() {
        proofController.addProof(0L, user, "dummy", "dummy");
        verify(questController).getQuestById(0L);
    }

    @Test
    public void addProofArchivesTakenQuest() {
        proofController.addProof(0L, user, "dummy", "dummy");
        verify(questController).archiveTakenQuest(quest, user);
    }

    @Test
    public void getMyProofsControlsToken() {
        proofController.getMyProofs(token);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getMyProofsAsksForProofs() {
        proofController.getMyProofs(token);
        verify(proofService).getUsersProofs(user);
    }

    @Test
    public void getUsersProofsControlsToken() {
        proofController.getUsersProofs(token, 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getUsersProofsFindsUser() {
        proofController.getUsersProofs(token, 0L);
        verify(userController).getUserById(0L);
    }

    @Test
    public void getUsersProofsAsksForProofs() {
        proofController.getUsersProofs(token, 0L);
        verify(proofService).getUsersProofs(user);
    }

    @Test
    public void getQuestsProofsControlsToken() {
        proofController.getQuestsProofs(token, 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getQuestsProofsAsksForQuest() {
        proofController.getQuestsProofs(token, 0L);
        verify(questController).getQuestById(0L);
    }

    @Test
    public void getQuestsProofsAsksForProofs() {
        proofController.getQuestsProofs(token, 0L);
        verify(proofService).getQuestsProofs(quest);
    }

    @Test
    public void getAllProofsControlsToken() {
        proofController.getAllProofs(token);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getAllProofsCAsksForProofs() {
        proofController.getAllProofs(token);
        verify(proofService).getAllProofs();
    }

    @Test
    public void getProofByIdControlsToken() {
        proofController.getProofById(token, 0L);
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void getProofByIdAsksForProof() {
        proofController.getProofById(token, 0L);
        verify(proofService).getProofById(0L);
    }

    @Test
    public void getProofByIdSecondAsksForId() {
        proofController.getProofById(0L);
        verify(proofService).getProofById(0L);
    }
}