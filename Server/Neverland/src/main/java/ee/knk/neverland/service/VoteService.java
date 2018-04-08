package ee.knk.neverland.service;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.entity.Vote;

import java.util.Optional;

public interface VoteService {
    Vote addVote(Vote vote);

    int getProofPositiveRating(Proof proof);

    int getProofNegativeRating(Proof proof);

    Optional<Vote> getUsersVote(User user, Proof proof);
}
