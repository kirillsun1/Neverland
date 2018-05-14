package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.*;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.ProofService;
import ee.knk.neverland.tools.ProofPacker;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.PackageElement;
import java.util.*;

@RestController
public class ProofController {
    private final TokenController tokenController;
    private final ProofService proofService;
    private final QuestController questController;
    private final UserController userController;
    private final VoteController voteController;
    private final SubscriptionController subscriptionController;
    private final FollowingController followingController;
    private Gson gson = new Gson();

    @Autowired
    public ProofController(TokenController tokenController,
                           ProofService proofService,
                           QuestController questService,
                           UserController userController,
                           VoteController voteController,
                           SubscriptionController subscriptionController,
                           FollowingController followingController) {
        this.tokenController = tokenController;
        this.proofService = proofService;
        this.questController = questService;
        this.userController = userController;
        this.voteController = voteController;
        this.subscriptionController = subscriptionController;
        this.followingController = followingController;
    }

    void addProof(Long questId, User user, String path, String comment) {
        Quest quest = questController.getQuestById(questId);
        proofService.addProof(new Proof(user, quest, path, comment));
        questController.archiveTakenQuest(quest, user);
    }

    @RequestMapping(value = "/getMyProofs")
    public String getMyProofs(@RequestParam(name = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getUsersProofs(user.get());
        ProofPacker packer = new ProofPacker(voteController, questController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    @RequestMapping(value = "/getUsersProofs")
    public String getUsersProofs(@RequestParam(name = "token") String token,
                                 @RequestParam(name = "uid") Long id) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<User> user = userController.getUserById(id);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        List<Proof> proofs = proofService.getUsersProofs(user.get());
        ProofPacker packer = new ProofPacker(voteController, questController, me.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    @RequestMapping(value = "/getQuestsProofs")
    public String getQuestsProofs(@RequestParam(name = "token") String token,
                                  @RequestParam(name = "qid") Long questId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getQuestsProofs(questController.getQuestById(questId));
        ProofPacker packer = new ProofPacker(voteController, questController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    @RequestMapping(value = "/getAllProofs")
    public String getAllProofs(@RequestParam(name = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getAllProofs();
        ProofPacker packer = new ProofPacker(voteController, questController, user.get());
        return gson.toJson(new ListAnswer(packer.packNonPrivateProofs(proofs)));
    }

    @RequestMapping(value = "/getProofById")
    public String getProofById(@RequestParam(name = "token") String token,
                               @RequestParam(name = "pid") Long proofId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<Proof> proof = getProofById(proofId);
        if (!proof.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        ProofPacker packer = new ProofPacker(voteController, questController, user.get());
        return gson.toJson(packer.packProof(proof.get()));
    }

    @RequestMapping(value = "/getMyGroupsProofs")
    public String getMyGroupsProofs(@RequestParam(value = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<PeopleGroup> myGroups = subscriptionController.findUsersGroups(user.get());
        List<Quest> quests = questController.getQuestsFromGroups(myGroups);
        List<Proof> proofs = new ArrayList<>();
        quests.forEach(quest -> proofs.addAll(proofService.getQuestsProofs(quest)));
        if (proofs.size() > 0) {
            proofs.sort(Comparator.comparingLong(Proof::getId));
            Collections.reverse(proofs);
        }
        ProofPacker packer = new ProofPacker(voteController, questController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    @RequestMapping(value = "/getMyFollowingsProofs")
    public String getMyFollowingsProofs(@RequestParam(value = "token") String token) {
        Optional<User> me = tokenController.getTokenUser(token);
        if (!me.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<User> myFollowings = followingController.findUsersFollowings(me.get());
        List<Proof> proofs = new ArrayList<>();
        myFollowings.forEach(user -> proofs.addAll(proofService.getUsersProofs(user)));
        if (proofs.size() > 0) {
            proofs.sort(Comparator.comparingLong(Proof::getId));
            Collections.reverse(proofs);
        }
        ProofPacker packer = new ProofPacker(voteController, questController, me.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    Optional<Proof> getProofById(Long id) {
        return proofService.getProofById(id);
    }

    public boolean proofExists(Long questId, User user) {

        return proofService.existsProofWithUserAndQuest(user, questController.getQuestById(questId));
    }
}
