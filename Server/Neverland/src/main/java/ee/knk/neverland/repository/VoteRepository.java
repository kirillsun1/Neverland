package ee.knk.neverland.repository;


import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long>{
    @Query("SELECT Count(vote) FROM Vote vote WHERE vote.proof = :proof AND vote.agreed = true")
    int getProofPositiveRating(@Param("proof") Proof proof);

    @Query("SELECT Count(vote) FROM Vote vote WHERE vote.proof = :proof AND vote.agreed = false")
    int getProofNegativeRating(@Param("proof") Proof proof);

    @Query("SELECT vote FROM Vote vote WHERE vote.proof = :proof AND vote.user = :user")
    Optional<Vote> getUsersVote(@Param("user") User user, @Param("proof") Proof proof);
}
