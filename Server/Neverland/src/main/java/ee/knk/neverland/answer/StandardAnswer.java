package ee.knk.neverland.answer;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.constants.Constants;
import lombok.Data;

@Data
public class StandardAnswer {
    private Pojo body;
    private int code = Constants.SUCCEED;
    private Long uid;

    public StandardAnswer(Pojo body) {
        this.body = body;
    }

    public StandardAnswer(Pojo body, int code) {
        this.body = body;
        this.code = code;
    }

    public StandardAnswer(int code) {
        this.code = code;
    }

    public StandardAnswer(int code, Long userId) {
        this.code = code;
        this.uid = userId;
    }

    public boolean isSuccessful() {
        return code > 0;
    }
}
