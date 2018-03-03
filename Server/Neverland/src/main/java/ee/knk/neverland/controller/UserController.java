package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.RegistrationLoginAnswer;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ee.knk.neverland.constants.RegistrationLoginConstants;


import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final TokenController tokenController;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenController = new TokenController(tokenService);
    }

    @RequestMapping(value="/register")
    public String register(@RequestParam(value="username") String username, @RequestParam(value="password") String password, @RequestParam(value="email") String email) {
        Gson gson = new Gson();
        if (userService.existsWithUsernameOrEmail(username, email)) {
            return gson.toJson(new RegistrationLoginAnswer(RegistrationLoginConstants.FAILED_REGISTRATION));
        }
        User user = userService.addUser(new User(username, password, email));
        return gson.toJson(new RegistrationLoginAnswer(RegistrationLoginConstants.SUCCEED_REGISTRATION, tokenController.addKey(user)));
    }

    @RequestMapping(value="/login")
    public String login(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {
        Gson gson = new Gson();
        Optional<User> userOp = userService.findMatch(username, password);
        if (!userOp.isPresent()) {
            return gson.toJson(new RegistrationLoginAnswer(RegistrationLoginConstants.FAILED_LOGIN));
        }
        String token = tokenController.addKey(userOp.get());
        return gson.toJson(new RegistrationLoginAnswer(RegistrationLoginConstants.SUCCEED_REGISTRATION, token));
    }


}
