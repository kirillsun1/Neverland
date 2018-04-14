package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.ProofRepository;
import ee.knk.neverland.service.ProofService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProofServiceImpl implements ProofService {

    private final ProofRepository proofRepository;

    @Autowired
    public ProofServiceImpl(ProofRepository proofRepository) {
        this.proofRepository = proofRepository;
    }
    @Override
    public Proof addProof(Proof proof) {
        return proofRepository.saveAndFlush(proof);
    }

    @Override
    public List<Proof> getUsersProofs(User user) {
        return proofRepository.getProofsByUser(user);
    }

    @Override
    public List<Proof> getQuestsProofs(Quest quest) {
        return proofRepository.getProofsByQuest(quest);
    }

    @Override
    public List<Proof> getAllProofs() {
        return proofRepository.findAllAndSort();
    }

    @Override
    public Optional<Proof> getProofById(Long id) {
        return proofRepository.findOneIfExists(id) ;
    }
}