package ee.knk.neverland.tools;

public class Validator {
    public boolean loginIsCorrect(String login) {
        return login.matches("^[a-z0-9_-]{6,16}$");
    }

    public boolean emailIsCorrect(String email){
        return email.matches("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$");
    }

    public boolean nameIsCorrect(String name) {
        return name.matches("^[A-Za-z ,.'-]+$");
    }
}
