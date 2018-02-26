package ee.knk.neverland.answer_pojo;


public class RegistrationLoginAnswer {
    private final String message;
    private final int code;
    private final String keyValue;

    public RegistrationLoginAnswer(int code, String message,String key) {
        this.code = code;
        this.message = message;
        this.keyValue = key;
    }

    public RegistrationLoginAnswer(int code, String message) {
        this.code = code;
        this.message = message;
        this.keyValue = "";
    }

}
