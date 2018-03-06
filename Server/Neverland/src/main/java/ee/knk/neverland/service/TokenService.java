package ee.knk.neverland.service;

import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;

import java.util.List;
import java.util.Optional;

public interface TokenService {

    Token addToken(Token token);
    void cleanOutByUser(User user);
    boolean exists(String tokenValue);
    List<Token> getAll();
    boolean isValid(String tokenValue);
    Optional<User> getTokenUser(String value);
}
