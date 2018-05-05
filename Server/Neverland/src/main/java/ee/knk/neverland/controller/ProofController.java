package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.ProofList;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.ProofService;
import ee.knk.neverland.tools.ProofPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProofController {
    private final TokenController tokenController;
    private final ProofService proofService;
    private final QuestService questService;
    private final TakenQuestController takenQuestController;
    private Gson gson = new Gson();

    @Autowired
    public ProofController(TokenController tokenController, ProofService proofService, QuestService questService, TakenQuestController takenQuestController) {
        this.tokenController = tokenController;
        this.proofService = proofService;
        this.questService = questService;
        this.takenQuestController = takenQuestController;
    }

    void addProof(Long questId, User user, String path, String comment) {
        Quest quest = questService.getQuestById(questId);
        proofService.addProof(new Proof(user, quest, path, comment));
        takenQuestController.archiveTakenQuest(quest, user);
    }

    @RequestMapping(value = "/getMyProofs")
    public String getUsersProofs(@RequestParam(name = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getUsersProofs(user.get());
        ProofPacker packer = new ProofPacker();
        return gson.toJson(packer.packProofs(proofs));
    }

    @RequestMapping(value = "/getQuestsProofs")
    public String getQuestsProofs(@RequestParam(name = "token") String token, @RequestParam(name = "qid") Long questId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getQuestsProofs(questService.getQuestById(questId));
        ProofPacker packer = new ProofPacker();
        return gson.toJson(packer.packProofs(proofs));
    }

    @RequestMapping(value = "/getAllProofs")
    public String getAllProofs(@RequestParam(name = "token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        List<Proof> proofs = proofService.getAllProofs();
        ProofPacker packer = new ProofPacker();
        return gson.toJson(packer.packProofs(proofs));
    }
}
