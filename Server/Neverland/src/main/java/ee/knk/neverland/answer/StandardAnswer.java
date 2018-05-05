package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;


public class StandardAnswer {
    private final int code;
    @SerializedName("token")
    private final String tokenValue;

    public StandardAnswer(int code, String token) {
        this.code = code;
        this.tokenValue = token;
    }

    public StandardAnswer(int code) {
        this.code = code;
        this.tokenValue = "";
    }

}
