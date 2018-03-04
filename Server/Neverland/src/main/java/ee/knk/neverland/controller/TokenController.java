package ee.knk.neverland.controller;


import com.google.common.hash.Hashing;
import ee.knk.neverland.entity.Token;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TokenService;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Controller
class TokenController {

    private final TokenService keyService;

    public TokenController(TokenService keyService) {
        this.keyService = keyService;
    }


    String addKey(User user) {
        String keyValue;
        do {
            keyValue = generateValue(user.getName());
        } while (keyService.checkToken(keyValue));
        Token token = new Token(user, keyValue);
        keyService.addToken(token);
        return token.getKeyValue();
    }

    private String generateValue(String username) {
        LocalDateTime time = LocalDateTime.now();
        String toEncode = time.toString() + username;
        return Hashing.sha256()
                .hashString(toEncode, StandardCharsets.UTF_8)
                .toString();
    }


}
