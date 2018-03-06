package ee.knk.neverland.controller;


import com.google.common.hash.Hashing;
import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TokenService;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    boolean isRight(String token) {
        return tokenService.isValid(token);

    }

    String addKey(User user) {
        String tokenValue;
        do {
            tokenValue = generateValue(user.getUsername());
        } while (tokenService.exists(tokenValue));
        Token token = new Token(user, tokenValue);
        tokenService.addToken(token);
        return token.getValue();
    }

    private String generateValue(String username) {
        LocalDateTime time = LocalDateTime.now();
        String toEncode = time.toString() + username;
        return Hashing.sha256()
                .hashString(toEncode, StandardCharsets.UTF_8)
                .toString();
    }

    Optional<User> getTokenUser(String value) {
        return tokenService.getTokenUser(value);
    }


}
