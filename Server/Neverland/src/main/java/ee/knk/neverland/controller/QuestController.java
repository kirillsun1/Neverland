package ee.knk.neverland.controller;


import com.google.gson.Gson;
import ee.knk.neverland.answer.RegistrationLoginAnswer;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.QuestService;
import ee.knk.neverland.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ee.knk.neverland.constants.RegistrationLoginConstants;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class QuestController {
    private final QuestService questService;
    private final TokenController tokenController;
    private Gson gson = new Gson();

    public QuestController(QuestService questService, TokenService tokenService) {
        this.questService = questService;
        this.tokenController = new TokenController(tokenService);
    }

    @RequestMapping(value="/submitquest", method = RequestMethod.POST, headers = {"Content-type=application/x-www-form-urlencoded;charset=UTF-8"})
    public String postQuest(@RequestParam(value="token") String token, @RequestParam(value = "title") String title, @RequestParam(value = "desc") String description, @RequestParam(value = "gid") Long groupId) {
        Optional<User> userPk = tokenController.getTokenUser(token);
        if (!userPk.isPresent()) {
            return gson.toJson(new RegistrationLoginAnswer(RegistrationLoginConstants.FAILED));
        }
        questService.addQuest(new Quest(title, description, userPk.get(), groupId));
        return gson.toJson(new RegistrationLoginAnswer(RegistrationLoginConstants.SUCCEED));
    }

}
