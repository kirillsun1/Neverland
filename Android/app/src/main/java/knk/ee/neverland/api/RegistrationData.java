package knk.ee.neverland.api;

public class RegistrationData {
    private String login;
    private String password;
    private String firstName;
    private String secondName;
    private String email;

    public final String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public final String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public final String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public final String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public final String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
