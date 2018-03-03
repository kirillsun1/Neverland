package ee.knk.neverland.answer;

import com.google.gson.annotations.SerializedName;


public class RegistrationLoginAnswer {
    private final int code;
    @SerializedName("token")
    private final String tokenValue;

    public RegistrationLoginAnswer(int code, String token) {
        this.code = code;
        this.tokenValue = token;
    }

    public RegistrationLoginAnswer(int code) {
        this.code = code;
        this.tokenValue = "";
    }

}
