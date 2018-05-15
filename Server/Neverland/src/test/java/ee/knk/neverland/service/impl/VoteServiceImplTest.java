package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.entity.Vote;
import ee.knk.neverland.repository.VoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VoteServiceImplTest {

    @Mock
    private VoteRepository voteRepository;
    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private User user;

    @Mock
    private Vote vote;

    @Mock
    private Proof proof;

    @Test
    public void testIfAddVoteChecksRightToVote() {
        when(voteRepository.getUsersVote(any(), any())).thenReturn(Optional.empty());
        when(voteRepository.saveAndFlush(any())).thenReturn(vote);
        voteService.addVote(vote);
        verify(voteRepository).getUsersVote(any(), any());
    }


    @Test
    public void testIfGetProofPositiveRatingReturnsRepoValue() {
        when(voteRepository.getProofPositiveRating(any())).thenReturn(10);
        assertEquals(10, voteService.getProofPositiveRating(proof));
    }


    @Test
    public void testIfGetProofNegativeRatingReturnsRepoValue() {
        when(voteRepository.getProofNegativeRating(any())).thenReturn(12);
        assertEquals(12, voteService.getProofNegativeRating(proof));
    }

    @Test
    public void testIfGetUsersVoteReturnsRepoValue() {
        when(voteRepository.getUsersVote(any(), any())).thenReturn(Optional.empty());
        voteService.getUsersVote(user, proof);
        verify(voteRepository).getUsersVote(any(), any());
    }

}
