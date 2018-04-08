package ee.knk.neverland.tools;

public class Validator {
    public boolean loginIsCorrect(String login) {
        return login.matches("(?!^[0-9]*\\$)^([-_a-zA-Z0-9]{6,16})\\$");
    }

    public boolean emailIsCorrect(String email){
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }

    public boolean nameIsCorrect(String name) {
        return name.matches("^[A-Za-z ,.'-]+$");
    }
}
