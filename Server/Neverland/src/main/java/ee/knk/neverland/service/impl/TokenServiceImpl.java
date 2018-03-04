package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.TokenRepository;
import ee.knk.neverland.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token addToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public void cleanOutByUser(User user) {
        tokenRepository.clearUpOutOfDateKeys(user);
    }

    @Override
    public boolean exists(String tokenValue) {
        return tokenRepository.exists(tokenValue).isPresent();
    }

    @Override
    public List<Token> getAll() {
        return tokenRepository.findAll();
    }

    @Override
    public boolean isValid(String tokenValue) {
        return tokenRepository.isValid(tokenValue).isPresent();
    }

}
