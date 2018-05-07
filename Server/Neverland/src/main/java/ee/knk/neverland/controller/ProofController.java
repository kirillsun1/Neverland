package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
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
    private final TakenQuestController takenQuestController;
    private final UserController userController;
    private final VoteController voteController;
    private final SubscriptionController subscriptionController;
    private Gson gson = new Gson();

    @Autowired
    public ProofController(TokenController tokenController,
                           ProofService proofService,
                           QuestController questService,
                           UserController userController,
                           TakenQuestController takenQuestController,
                           VoteController voteController,
                           SubscriptionController subscriptionController) {
        this.tokenController = tokenController;
        this.proofService = proofService;
        this.questController = questService;
        this.takenQuestController = takenQuestController;
        this.userController = userController;
        this.voteController = voteController;
        this.subscriptionController = subscriptionController;
    }

    void addProof(Long questId, User user, String path, String comment) {
        Quest quest = questController.getQuestById(questId);
        proofService.addProof(new Proof(user, quest, path, comment));
        takenQuestController.archiveTakenQuest(quest, user);
    }

    @RequestMapping(value = "/getMyProofs")
    public String getMyProofs(@RequestParam(name = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getUsersProofs(user.get());
        ProofPacker packer = new ProofPacker(voteController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    @RequestMapping(value = "/getUsersProofs")
    public String getUsersProofs(@RequestParam(name = "token") String token,
                                 @RequestParam(name = "uid") Long id) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getUsersProofs(userController.getUserById(id));
        ProofPacker packer = new ProofPacker(voteController, user.get());
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
        ProofPacker packer = new ProofPacker(voteController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    @RequestMapping(value = "/getAllProofs")
    public String getAllProofs(@RequestParam(name = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getAllProofs();
        ProofPacker packer = new ProofPacker(voteController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
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
        ProofPacker packer = new ProofPacker(voteController, user.get());
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
        ProofPacker packer = new ProofPacker(voteController, user.get());
        return gson.toJson(new ListAnswer(packer.packAllProofs(proofs)));
    }

    Optional<Proof> getProofById(Long id) {
        return proofService.getProofById(id);
    }
}
