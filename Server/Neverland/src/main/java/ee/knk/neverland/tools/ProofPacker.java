package ee.knk.neverland.tools;

import ee.knk.neverland.answer.ProofList;
import ee.knk.neverland.answer.ProofPojo;
import ee.knk.neverland.answer.UserPojo;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;

import java.util.List;

public class ProofPacker {
    private QuestPacker questPacker = new QuestPacker();
    public ProofList packProofs(List<Proof> proofs) {
        ProofList packedProofs = new ProofList();
        for (Proof pointer : proofs) {
            ProofPojo packedProof = packProof(pointer);
            packedProofs.proofs.add(packedProof);
        }
        return packedProofs;
    }

    private ProofPojo packProof(Proof pointer) {
        ProofPojo neededData = new ProofPojo();
        neededData.quest = questPacker.packQuest(pointer.getQuest());
        UserPojo user = new UserPojo();
        user.userId = pointer.getUser().getId();
        user.username = pointer.getUser().getUsername();
        user.firstName = pointer.getUser().getFirstName();
        user.secondName = pointer.getUser().getSecondName();
        user.avatar = pointer.getUser().getAvatarPath();
        neededData.proofer = user;
        neededData.comment = pointer.getComment();
        neededData.id = pointer.getId();
        neededData.picturePath = pointer.getPicturePath();
        neededData.time = pointer.getTime();
        return neededData;
    }
}