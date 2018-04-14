package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.entity.Vote;
import ee.knk.neverland.repository.VoteRepository;
import ee.knk.neverland.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }
    @Override
    public Vote addVote(Vote vote) {
        if (voteRepository.getUsersVote(vote.getUser(), vote.getProof()).isPresent()) {
            return vote;
        }
        return voteRepository.saveAndFlush(vote);
    }

    @Override
    public int getProofPositiveRating(Proof proof) {
        return voteRepository.getProofPositiveRating(proof);
    }

    @Override
    public int getProofNegativeRating(Proof proof) {
        return voteRepository.getProofNegativeRating(proof);
    }

    @Override
    public Optional<Vote> getUsersVote(User user, Proof proof) {

        return voteRepository.getUsersVote(user, proof);
    }
}
