package ee.knk.neverland.controller;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.ProofService;
import ee.knk.neverland.service.VoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class VoteControllerTest {

    @Mock
    private VoteService voteService;
    @Mock
    private ProofService proofService;
    @Mock
    private TokenController tokenController;
    @InjectMocks
    private VoteController voteController;

    @Mock
    private User user;
    @Mock
    private Proof proof;

    @Before
    public void before() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(proofService.getProofById(0L)).thenReturn(Optional.of(proof));
        when(voteService.ifUserCanVoteForProof(any(), any())).thenReturn(true);
        when(voteService.getProofPositiveRating(any())).thenReturn(0);
        when(voteService.getProofNegativeRating(any())).thenReturn(0);
        when(voteService.getUsersVote(any(), any())).thenReturn(Optional.empty());
    }

    @Test
    public void testIfVoteCallsTokenCheck() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.empty());
        voteController.vote("token", 0L, true);
        verify(tokenController).getTokenUser("token");
    }

    @Test
    public void testIfVoteCallsGetProofById() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(proofService.getProofById(0L)).thenReturn(Optional.empty());
        voteController.vote("token", 0L, true);
        verify(proofService).getProofById(0L);
    }

    @Test
    public void testIfVoteCheckUsersRightToVote() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(proofService.getProofById(0L)).thenReturn(Optional.of(proof));
        when(voteService.ifUserCanVoteForProof(any(), any())).thenReturn(false);
        voteController.vote("token", 0L, true);
        verify(voteService).ifUserCanVoteForProof(any(), any());
    }

    @Test
    public void testIfVoteAddsVote() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));
        when(proofService.getProofById(0L)).thenReturn(Optional.of(proof));
        when(voteService.ifUserCanVoteForProof(any(), any())).thenReturn(true);
        voteController.vote("token", 0L, true);
        verify(voteService).addVote(any());
    }

    @Test
    public void testIfVoteAsksForPositiveRating() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));        when(proofService.getProofById(0L)).thenReturn(Optional.of(proof));
        when(voteService.ifUserCanVoteForProof(any(), any())).thenReturn(true);
        when(voteService.getProofPositiveRating(any())).thenReturn(0);
        voteController.vote("token", 0L, true);
        verify(voteService).getProofPositiveRating(any());
    }

    @Test
    public void testIfVoteAsksForNegativeRating() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));        when(proofService.getProofById(0L)).thenReturn(Optional.of(proof));
        when(voteService.ifUserCanVoteForProof(any(), any())).thenReturn(true);
        when(voteService.getProofNegativeRating(any())).thenReturn(0);
        voteController.vote("token", 0L, true);
        verify(voteService).getProofNegativeRating(any());
    }

    @Test
    public void testIfVoteAsksForUsersVote() {
        when(tokenController.getTokenUser("token")).thenReturn(Optional.of(user));        when(proofService.getProofById(0L)).thenReturn(Optional.of(proof));
        when(voteService.ifUserCanVoteForProof(any(), any())).thenReturn(true);
        when(voteService.getUsersVote(any(), any())).thenReturn(Optional.empty());
        voteController.vote("token", 0L, true);
        verify(voteService).getUsersVote(any(), any());
    }

    @Test
    public void testIfGetUsersRatingReturnsZeroIfGetsZeroSizeProofs() {
        when(proofService.getUsersProofs(any())).thenReturn(new ArrayList<>());
        assertEquals(0, voteController.getUsersRating(user), 0.01);
    }

    @Test
    public void testIfGetUsersRatingReturnsPercentageIfGetsProofsWithRating() {
        List<Proof> proofs = new ArrayList<>();
        proofs.add(proof);
        when(voteService.getProofPositiveRating(proof)).thenReturn(10);
        when(voteService.getProofNegativeRating(proof)).thenReturn(10);
        when(proofService.getUsersProofs(any())).thenReturn(proofs);
        assertEquals(0.5, voteController.getUsersRating(user), 0.01);
    }


}
