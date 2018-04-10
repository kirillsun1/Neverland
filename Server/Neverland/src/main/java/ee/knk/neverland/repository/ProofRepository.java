package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProofRepository extends JpaRepository<Proof, Long> {
    @Query("select proof from Proof proof where proof.user = :user")
    List<Proof> getProofsByUser(User user);
    @Query("select proof from Proof proof where proof.quest = :user")
    List<Proof> getProofsByQuest(Quest quest);
}
