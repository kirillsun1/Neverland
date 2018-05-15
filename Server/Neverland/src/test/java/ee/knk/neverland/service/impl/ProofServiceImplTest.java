package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.GroupRepository;
import ee.knk.neverland.repository.ProofRepository;
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
public class ProofServiceImplTest {

    @Mock
    private ProofRepository proofRepository;
    @InjectMocks
    private ProofServiceImpl proofService;

    @Mock
    private User user;

    @Mock
    private Proof proof;
    @Mock
    private Quest quest;

    @Test
    public void testIfAddProofCallsRepo() {
        when(proofRepository.saveAndFlush(proof)).thenReturn(proof);
        proofService.addProof(proof);
        verify(proofRepository).saveAndFlush(proof);
    }

    @Test
    public void testIfGetUsersProofsCallsRepo() {
        when(proofRepository.getProofsByUser(user)).thenReturn(new ArrayList<>());
        proofService.getUsersProofs(user);
        verify(proofRepository).getProofsByUser(user);
    }

    @Test
    public void testIfGetQuestsProofsCallsRepo() {
        when(proofRepository.getProofsByQuest(quest)).thenReturn(new ArrayList<>());
        proofService.getQuestsProofs(quest);
        verify(proofRepository).getProofsByQuest(quest);
    }

    @Test
    public void testIfGetAllProofsCallsRepo() {
        when(proofRepository.findAllAndSort()).thenReturn(new ArrayList<>());
        proofService.getAllProofs();
        verify(proofRepository).findAllAndSort();
    }

    @Test
    public void testIfExistsProofWithUserAndQuestReturnsFalseIfRepoGivesEmptyOptional() {
        when(proofRepository.findProofByUserAndQuest(user, quest)).thenReturn(Optional.empty());
        assert(!proofService.existsProofWithUserAndQuest(user, quest));
    }

    @Test
    public void testIfExistsProofWithUserAndQuestReturnsTrueIfRepoGivesObjectInOptional() {
        when(proofRepository.findProofByUserAndQuest(user, quest)).thenReturn(Optional.of(proof));
        assert(proofService.existsProofWithUserAndQuest(user, quest));
    }
}
