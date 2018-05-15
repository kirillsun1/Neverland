package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.RatingPojo;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.entity.Vote;
import ee.knk.neverland.service.ProofService;
import ee.knk.neverland.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class VoteController {

    private final VoteService voteService;
    private final TokenController tokenController;
    private final Gson gson = new Gson();
    private final ProofService proofService;

    @Autowired
    public VoteController(VoteService voteService, TokenController tokenController, ProofService proofController) {
        this.voteService = voteService;
        this.tokenController = tokenController;
        this.proofService = proofController;
    }

    @RequestMapping(value = "/vote")
    public String vote(@RequestParam(name = "token") String token,
                               @RequestParam(name = "pid") Long proofId,
                               @RequestParam(name = "value") boolean value) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<Proof> proof = getProofById(proofId);
        if (!proof.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        if (!voteService.ifUserCanVoteForProof(user.get(), proof.get())) {
            return gson.toJson(new StandardAnswer(Constants.PERMISSION_DENIED));
        }
        Vote vote = new Vote(user.get(), proof.get(), value);
        voteService.addVote(vote);
        RatingPojo rating = new RatingPojo(getProofPositiveRating(proof.get()),
                getProofNegativeRating(proof.get()),
                getUsersVote(user.get(), proof.get()));
        return gson.toJson(new StandardAnswer(rating));
    }

    private Optional<Proof> getProofById(Long proofId) {
        return proofService.getProofById(proofId);
    }

    public int getProofPositiveRating(Proof proof) {
        return voteService.getProofPositiveRating(proof);
    }

    public int getProofNegativeRating(Proof proof) {
        return voteService.getProofNegativeRating(proof);
    }

    public int getUsersVote(User user, Proof proof) {
        Optional<Vote> vote = voteService.getUsersVote(user, proof);
        if (!vote.isPresent()) {
            return Constants.USER_DID_NOT_VOTE;
        }
        if (vote.get().isAgreed()) {
            return Constants.USER_AGREED;
        }
        return Constants.USER_DISAGREED;
    }

    public double getUsersRating(User user) {
        List<Proof> proofs = proofService.getUsersProofs(user);
        int positive = proofs.stream()
                .mapToInt(voteService::getProofPositiveRating)
                .sum();
        int negative = proofs.stream()
                .mapToInt(voteService::getProofNegativeRating)
                .sum();
        return positive / (positive + negative);
    }
}
