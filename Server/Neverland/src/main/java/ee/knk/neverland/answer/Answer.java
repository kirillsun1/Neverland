package ee.knk.neverland.answer;

import ee.knk.neverland.answer.pojo.Pojo;

public class Answer {
    private Pojo body;
    private int code;

    public Answer(Pojo body, int code) {
        this.body = body;
        this.code = code;
    }

    public Answer(int code) {
        this.code = code;
    }
}
