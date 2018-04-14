package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProofRepository extends JpaRepository<Proof, Long> {
    @Query("SELECT proof FROM Proof proof WHERE proof.user = :user ORDER BY proof.id DESC")
    List<Proof> getProofsByUser(@Param("user") User user);

    @Query("SELECT proof FROM Proof proof WHERE proof.quest = :quest ORDER BY proof.id DESC")
    List<Proof> getProofsByQuest(@Param("quest") Quest quest);

    @Query("SELECT proof FROM Proof proof WHERE proof.id = :id")
    Optional<Proof> findOneIfExists(@Param("id") Long id);

    @Query("SELECT proof FROM Proof proof ORDER BY proof.id DESC")
    List<Proof> findAllAndSort();
}
