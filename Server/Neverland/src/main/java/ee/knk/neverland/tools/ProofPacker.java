package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.ProofPojo;
import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.answer.pojo.builder.ProofPojoBuilder;
import ee.knk.neverland.controller.QuestController;
import ee.knk.neverland.controller.VoteController;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ProofPacker {
    private final VoteController voteController;
    private final User me;
    private final QuestPacker questPacker;
    private UserPacker userPacker;


    public ProofPacker(VoteController voteController, QuestController questController, User me) {
        this.voteController = voteController;
        this.questPacker = new QuestPacker(questController, me);
        this.userPacker = new UserPacker();
        this.me = me;
    }

    public ProofPojo packProof(Proof pointer) {
        UserPojo proofer = userPacker.packUser(pointer.getUser());
        QuestPojo quest = questPacker.packQuest(pointer.getQuest());
        ProofPojoBuilder builder = new ProofPojoBuilder();
        return builder
                .withId(pointer.getId())
                .withComment(pointer.getComment())
                .withProofer(proofer)
                .withPicturePath(pointer.getPicturePath())
                .withQuest(quest)
                .withTime(pointer.getTime())
                .withRating(voteController.getProofPositiveRating(pointer),
                        voteController.getProofNegativeRating(pointer),
                        voteController.getUsersVote(me, pointer))
                .getProofPojo();
    }

    public List<Pojo> packNonPrivateProofs(List<Proof> proofs) {
        List<Pojo> packedProofs = new ArrayList<>();
        for (Proof pointer : proofs) {
            if(pointer.getQuest().getPeopleGroup() == null) {
                packedProofs.add(packProof(pointer));
            }
        }
        return packedProofs;
    }

    public List<Pojo> packAllProofs(List<Proof> proofs) {
        List<Pojo> packedProofs = new ArrayList<>();
        for (Proof pointer : proofs) {
            packedProofs.add(packProof(pointer));
        }
        return packedProofs;
    }
}
