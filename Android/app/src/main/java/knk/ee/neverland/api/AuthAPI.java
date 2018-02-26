package knk.ee.neverland.api;

import knk.ee.neverland.exceptions.LoginFailedException;
import knk.ee.neverland.exceptions.RegistrationFailedException;
import knk.ee.neverland.pojos.RegistrationData;

public interface AuthAPI {
    String attemptLogin(final String login, final String password) throws LoginFailedException;

    void registerAccount(final RegistrationData registrationData) throws RegistrationFailedException;

    boolean isKeyActive(String key);
}
