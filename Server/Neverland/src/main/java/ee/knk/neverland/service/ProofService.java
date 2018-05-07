package ee.knk.neverland.service;

import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProofService {

    Proof addProof(Proof proof);
    List<Proof> getUsersProofs(User user);
    List<Proof> getQuestsProofs(Quest quest);

    List<Proof> getAllProofs();
    Optional<Proof> getProofById(Long id);
}
