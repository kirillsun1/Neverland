package ee.knk.neverland.service;

import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface TokenService {

    Token addToken(Token token);
    void cleanOutByUser(User user);
    boolean checkToken(String keyText);
    List<Token> getAll();
}
