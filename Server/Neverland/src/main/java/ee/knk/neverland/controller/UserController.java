package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.RegistrationAnswer;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.FollowingService;
import ee.knk.neverland.service.TokenService;
import ee.knk.neverland.service.UserService;
import ee.knk.neverland.tools.UserPacker;
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
    private final FollowingController followingController;
    private final VoteController voteController;
    private Validator validator = new Validator();
    private Gson gson = new Gson();

    @Autowired
    public UserController(UserService userService,
                          TokenService tokenService,
                          FollowingService  followingService,
                          VoteController voteController) {
        this.userService = userService;
        this.tokenController = new TokenController(tokenService);
        this.followingController = new FollowingController(followingService, this, tokenController);
        this.voteController = voteController;
    }

    @RequestMapping(value="/register")
    public String register(@RequestParam(value="username") String username,
                           @RequestParam(value="password") String password,
                           @RequestParam(value="email") String email,
                           @RequestParam(value="firstname") String firstName,
                           @RequestParam(value="secondname") String secondName) {
        if (!(validator.loginIsCorrect(username) && validator.emailIsCorrect(email) && validator.nameIsCorrect(firstName) && validator.nameIsCorrect(secondName))
                || userService.existsWithUsernameOrEmail(username, email)) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        User user = userService.addUser(new User(username, password, email, firstName, secondName));
        return gson.toJson(new RegistrationAnswer(tokenController.addKey(user)));
    }

    @RequestMapping(value="/login")
    public String login(@RequestParam(value="username") String username,
                        @RequestParam(value="password") String password) {
        Optional<User> user = userService.findMatch(username, password);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        String token = tokenController.addKey(user.get());
        return gson.toJson(new RegistrationAnswer(token, user.get().getId()));
    }

    @RequestMapping(value="/tokenCheck")
    public StandardAnswer checkToken(@RequestParam(value="token") String token) {
        Optional<User> user = tokenController.isValid(token);
        return user.map(user1 -> new StandardAnswer(Constants.SUCCEED, user1.getId())).orElseGet(() -> new StandardAnswer(Constants.FAILED));
    }

    @RequestMapping(value="/getUsersInfo")
    public String getUsersInfo(@RequestParam(value="token") String token,
                               @RequestParam(value = "uid") Long id) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        UserPacker packer = new UserPacker();
        Optional<User> toFind = getUserById(id);
        if (!toFind.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        return gson.toJson(packer.packDetailedAnotherUser(toFind.get(), followingController, voteController, user.get()));
    }

    @RequestMapping(value="/getMyInfo")
    public String getMyInfo(@RequestParam(value="token") String token) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        UserPacker packer = new UserPacker();
        return gson.toJson(packer.packDetailedMe(user.get(), followingController, voteController));
    }

    void setAvatar(Long id, String path) {
        userService.setAvatar(id, path);
    }

    Optional<User> getUserById(Long id) {
        return userService.findUser(id);
    }
}
