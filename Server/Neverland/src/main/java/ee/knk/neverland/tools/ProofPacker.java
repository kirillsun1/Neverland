package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.ProofPojo;
import ee.knk.neverland.answer.pojo.ProofPojo.ProofPojoBuilder;
import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.controller.VoteController;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ProofPacker {
    private final VoteController voteController;
    private final User me;
    private UserPacker userPacker = new UserPacker();
    private QuestPacker questPacker = new QuestPacker();


    public ProofPacker(VoteController voteController, User me) {
        this.voteController = voteController;
        this.me = me;
    }

    private ProofPojo packProof(Proof pointer) {
        UserPojo proofer = userPacker.packUser(pointer.getUser());
        QuestPojo quest = questPacker.packQuest(pointer.getQuest());
        ProofPojoBuilder builder = new ProofPojoBuilder();
        return builder
                .setId(pointer.getId())
                .setComment(pointer.getComment())
                .setProofer(proofer)
                .setPicturePath(pointer.getPicturePath())
                .setQuest(quest)
                .setTime(pointer.getTime())
                .setPositiveRating(voteController.getProofPositiveRating(pointer))
                .setNegativeRating(voteController.getProofNegativeRating(pointer))
                .setMyVote(voteController.getUsersVote(me, pointer))
                .getProofPojo();
    }

    public List<Pojo> packProofs(List<Proof> proofs) {
        List<Pojo> packedProofs = new ArrayList<>();
        for (Proof pointer : proofs) {
            packedProofs.add(packProof(pointer));
        }
        return packedProofs;
    }
}
