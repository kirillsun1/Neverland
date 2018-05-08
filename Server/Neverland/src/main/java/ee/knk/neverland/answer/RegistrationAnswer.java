package ee.knk.neverland.answer;


import ee.knk.neverland.constants.Constants;
import lombok.Data;

@Data
public class RegistrationAnswer {
    private int code = Constants.SUCCEED;
    private String token;
    private Long uid;

    public RegistrationAnswer(String token) {
        this.token = token;
    }

    public RegistrationAnswer(String token, Long uid) {
        this.token = token;
        this.uid = uid;
    }

    public RegistrationAnswer(int code) {
        this.code = code;
    }


}
