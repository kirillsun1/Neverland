package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProofPojo implements Pojo {
    private Long id;
    private String comment;
    @SerializedName("pic_path")
    private String picturePath;
    private QuestPojo quest;
    private UserPojo proofer;
    @SerializedName("add_time")
    private LocalDateTime time;
    @SerializedName("for")
    private int positiveRating;
    @SerializedName("against")
    private int negativeRating;
    @SerializedName("my_vote")
    private int myVote;


    public static class ProofPojoBuilder {
        private ProofPojo proofPojo = new ProofPojo();

        public ProofPojoBuilder setId(Long id) {
            proofPojo.id = id;
            return this;
        }

        public ProofPojoBuilder setComment(String comment) {
            proofPojo.comment = comment;
            return this;
        }

         public ProofPojoBuilder setPicturePath(String picturePath) {
            proofPojo.picturePath = picturePath;
            return this;
         }

         public ProofPojoBuilder setQuest(QuestPojo quest) {
            proofPojo.quest = quest;
            return this;
         }

         public ProofPojoBuilder setProofer(UserPojo proofer) {
            proofPojo.proofer = proofer;
            return this;
         }

         public ProofPojoBuilder setTime(LocalDateTime time) {
            proofPojo.time = time;
            return this;
         }

         public ProofPojoBuilder setPositiveRating(int votes) {
            proofPojo.positiveRating = votes;
            return this;
         }

        public ProofPojoBuilder setNegativeRating(int votes) {
            proofPojo.negativeRating = votes;
            return this;
        }

        public ProofPojoBuilder setMyVote(int vote) {
            proofPojo.myVote = vote;
            return this;
        }
         public ProofPojo getProofPojo() {
            return proofPojo;
        }
    }
}
