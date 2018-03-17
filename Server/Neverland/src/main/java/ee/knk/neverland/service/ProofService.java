package ee.knk.neverland.service;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface ProofService {

    Proof addProof(Proof proof);
    List<Proof> getUsersProofs(User user);
    List<Proof> getQuestsProofs(Quest quest);

    List<Proof> getAllProofs();
}
