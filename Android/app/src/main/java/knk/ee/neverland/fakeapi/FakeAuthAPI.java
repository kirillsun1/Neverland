package knk.ee.neverland.fakeapi;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import knk.ee.neverland.api.AuthAPI;
import knk.ee.neverland.pojos.RegistrationData;
import knk.ee.neverland.exceptions.LoginFailedException;
import knk.ee.neverland.exceptions.RegistrationFailedException;

public class FakeAuthAPI implements AuthAPI {
    private List<User> registeredUsers = new ArrayList<>();
    private List<String> workingKeys = new ArrayList<>();

    public FakeAuthAPI() {
        workingKeys.add("ABC");
    }

    @Override
    public String attemptLogin(final String login, final String password) throws LoginFailedException {
        for (User user : registeredUsers) {
            if (user.login.equals(login) && user.password.equals(password)) {
                String key = generateKey(user);
                workingKeys.add(key);
                return key;
            }
        }

        throw new LoginFailedException("User not found!");
    }

    @Override
    public void registerAccount(final RegistrationData registrationData) throws RegistrationFailedException {
        if (!loginIsCorrect(registrationData.getLogin())) {
            throw new RegistrationFailedException("Incorrect login.");
        }

        if (!passwordIsCorrect(registrationData.getPassword())) {
            throw new RegistrationFailedException("Incorrect password.");
        }

        if (!nameIsCorrect(registrationData.getFirstName())) {
            throw new RegistrationFailedException("Incorrect first name.");
        }

        if (!nameIsCorrect(registrationData.getSecondName())) {
            throw new RegistrationFailedException("Incorrect second name.");
        }

        if (!emailIsCorrect(registrationData.getEmail())) {
            throw new RegistrationFailedException("Incorrect email.");
        }

        User user = new User();

        user.login = registrationData.getLogin();
        user.password = registrationData.getPassword();
        user.firstName = registrationData.getFirstName();
        user.secondName = registrationData.getSecondName();
        user.email = registrationData.getEmail();

        registeredUsers.add(user);
    }

    @Override
    public boolean isKeyActive(String key) {
        return workingKeys.contains(key);
    }

    private String generateKey(User user) {
        String line = String.format("[%s==%s]", user.login, user.password);
        return Hashing.sha256()
                .hashString(line, StandardCharsets.UTF_8)
                .toString();
    }

    private boolean loginIsCorrect(String login) {
        return login.matches("^[a-z0-9_-]{6,16}$");
    }

    private boolean passwordIsCorrect(String password) {
        return password.matches("^[a-z0-9_-]{6,18}$");
    }

    private boolean emailIsCorrect(String email) {
        return email.matches("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$");
    }

    private boolean nameIsCorrect(String name) {
        return name.matches("^[A-Za-z ,.'-]+$");
    }

    private class User {
        String login;
        String password;
        String email;
        String firstName;
        String secondName;
    }
}
