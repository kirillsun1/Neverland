package ee.knk.neverland.controller;


import ee.knk.neverland.entity.Key;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
class KeyController {

    @Autowired
    private KeyService keyService;


    String addKey(User user) {
        Key key = new Key(user, generateValue(user.getName()));
        keyService.addKey(key);
        return key.getKeyValue();
    }

    private String generateValue(String username) {
        LocalDateTime time = LocalDateTime.now();
        return "some generated value";
    }


}
