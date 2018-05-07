package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RatingPojo {
    public RatingPojo(int positiveRating, int negativeRating, int myVote) {
        this.positiveRating = positiveRating;
        this.negativeRating = negativeRating;
        this.myVote = myVote;
    }
    @SerializedName("for")
    private int positiveRating;
    @SerializedName("against")
    private int negativeRating;
    @SerializedName("my_vote")
    private int myVote;
}
