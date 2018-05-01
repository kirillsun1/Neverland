package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RatingPojo {
    public RatingPojo(int positiveRating, int negativeRating) {
        this.positiveRating = positiveRating;
        this.negativeRating = negativeRating;
    }
    @SerializedName("for")
    private int positiveRating;
    @SerializedName("against")
    private int negativeRating;
}
