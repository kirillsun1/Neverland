package ee.knk.neverland.controller;


import ee.knk.neverland.answer_pojo.RegistrationLoginAnswer;
import ee.knk.neverland.entity.Key;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ee.knk.neverland.constants.RegistrationLoginConstants;


import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {
    private final UserService userService;
    private final KeyController keyController = new KeyController();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/register", method=POST)
    public RegistrationLoginAnswer register(@RequestParam(value="username") String username, @RequestParam(value="password") String password, @RequestParam(value="email") String email) {
        if (userService.findUserByName(username)) {
            return new RegistrationLoginAnswer(RegistrationLoginConstants.FAILED, RegistrationLoginConstants.REGISTRATION_FAILED_MESSAGE);
        }
        User user = userService.addUser(new User(username, password, email));
        return new RegistrationLoginAnswer(RegistrationLoginConstants.SUCCEED, RegistrationLoginConstants.REGISTRATION_SUCCEED_MESSAGE, keyController.addKey(user));
    }

    @RequestMapping(value="/login")
    public RegistrationLoginAnswer login(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {
        Optional<User> userOp = userService.findMatch(username, password);
        if (userOp.isPresent()) {
            return new RegistrationLoginAnswer(RegistrationLoginConstants.SUCCEED, RegistrationLoginConstants.LOGIN_SUCCEED_MESSAGE, keyController.addKey(userOp.get()));
        }
        return new RegistrationLoginAnswer(RegistrationLoginConstants.FAILED, RegistrationLoginConstants.LOGIN_FAILED_MESSAGE);
    }


}
