package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.service.UserService;
import ee.knk.neverland.tools.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ee.knk.neverland.constants.Constants;


import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final TokenController tokenController;
    private Validator validator = new Validator();
    private Gson gson = new Gson();

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenController = new TokenController(tokenService);
    }

    @RequestMapping(value="/register")
    public String register(@RequestParam(value="username") String username, @RequestParam(value="password") String password, @RequestParam(value="email") String email,
                           @RequestParam(value="firstname") String firstName, @RequestParam(value="secondname") String secondName) {
        if (!(validator.loginIsCorrect(username) && validator.emailIsCorrect(email) && validator.nameIsCorrect(firstName) && validator.nameIsCorrect(secondName))
                || userService.existsWithUsernameOrEmail(username, email)) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        User user = userService.addUser(new User(username, password, email, firstName, secondName));
        return gson.toJson(new StandardAnswer(Constants.SUCCEED, tokenController.addKey(user)));
    }

    @RequestMapping(value="/login")
    public String login(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {
        Optional<User> user = userService.findMatch(username, password);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        String token = tokenController.addKey(user.get());
        return gson.toJson(new StandardAnswer(Constants.SUCCEED, token));
    }

    @RequestMapping(value="/tokenCheck")
    public String checkToken(@RequestParam(value="token") String token) {
        if (tokenController.isValid(token)) {
            return gson.toJson(new StandardAnswer(Constants.SUCCEED));
        }
        return gson.toJson(new StandardAnswer(Constants.FAILED));
    }

    User getUserById(Long id) {
        return userService.findUser(id);
    }
}
