package ee.knk.neverland.controller;

import java.io.*;
import java.util.Optional;

import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.entity.PeopleGroup;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ee.knk.neverland.constants.Constants;

@RestController
public class FileUploadController {
    private final TokenController tokenController;
    private final ProofController proofController;
    private final GroupController groupController;
    private final UserController userController;
    private Gson gson = new Gson();

    @Autowired
    public FileUploadController(TokenController tokenController,
                                UserController userController,
                                ProofController proofController,
                                GroupController groupController) {
        this.tokenController = tokenController;
        this.proofController = proofController;
        this.groupController = groupController;
        this.userController = userController;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleProofUpload(@RequestParam("token") String token,
                             @RequestParam("qid") Long questId,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("comment") String comment) {
        String realPath = "/var/www/html/never_pictures/proofs/";
        String dbPath = "http://vrot.bounceme.net:8081/never_pictures/proofs/";
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        String pathEnding = user.get().getUsername() + "_" + questId + ".jpg";
        StandardAnswer standardAnswer = writeFile(file, realPath + pathEnding);
        if (standardAnswer.getCode() == Constants.SUCCEED) {
            proofController.addProof(questId, user.get(), dbPath + pathEnding, comment);
        }
        return gson.toJson(standardAnswer);

    }

    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public String handleUserAvatarUpload(@RequestParam("token") String token,
                                  @RequestParam("file") MultipartFile file) {
        String realPath = "/var/www/html/never_pictures/user_avatars/";
        String dbPath = "http://vrot.bounceme.net:8081/never_pictures/user_avatars/";
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        String pathEnding = user.get().getUsername() + ".jpg";
        StandardAnswer standardAnswer = writeFile(file, realPath + pathEnding);
        if (standardAnswer.getCode() == Constants.SUCCEED) {
            userController.setAvatar(user.get().getId(), dbPath + pathEnding);
        }
        return gson.toJson(standardAnswer);
    }

    @RequestMapping(value = "/uploadGroupAvatar", method = RequestMethod.POST)
    public String handleGroupAvatarUpload(@RequestParam("token") String token,
                                          @RequestParam("file") MultipartFile file,
                                          @RequestParam("gid") Long groupId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        String realPath = "/var/www/html/never_pictures/group_avatars/";
        String dbPath = "http://vrot.bounceme.net:8081/never_pictures/group_avatars/";
        String pathEnding = groupId + ".jpg";
        StandardAnswer standardAnswer = writeFile(file, realPath + pathEnding);
        if (standardAnswer.getCode() == Constants.SUCCEED) {
            groupController.setAvatar(groupId, dbPath + pathEnding);
        }

        return gson.toJson(standardAnswer);
    }

    private StandardAnswer writeFile(MultipartFile file, String realPath) {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(realPath)));
                stream.write(bytes);
                stream.close();
                return new StandardAnswer(Constants.SUCCEED);
            } catch (Exception e) {
                return new StandardAnswer(Constants.FILE_UPLOAD_FAILED);
            }
        } else {
            return new StandardAnswer(Constants.FILE_IS_EMPTY);
        }
    }
}