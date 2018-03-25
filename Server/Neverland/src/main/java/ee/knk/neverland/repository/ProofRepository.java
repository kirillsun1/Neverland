package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProofRepository extends JpaRepository<Proof, Long> {
    @Query("select proof from Proof proof where proof.user = :user order by proof.id desc")
    List<Proof> getProofsByUser(@Param("user") User user);
    @Query("select proof from Proof proof where proof.quest = :quest order by proof.id desc")
    List<Proof> getProofsByQuest(@Param("quest") Quest quest);
}
