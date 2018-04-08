package ee.knk.neverland.controller;

import java.io.*;
import java.util.Optional;

import com.google.gson.Gson;
import ee.knk.neverland.answer.Answer;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ee.knk.neverland.constants.Constants;

@RestController
public class FileUploadController {
    private final TokenController tokenController;
    private final ProofController proofController;
    private final GroupController groupController;
    private Gson gson = new Gson();

    @Autowired
    public FileUploadController(TokenService tokenService, ProofController proofController, GroupController groupController) {
        this.tokenController = new TokenController(tokenService);
        this.proofController = proofController;
        this.groupController = groupController;
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleProofUpload(@RequestParam("token") String token, @RequestParam("qid") Long questId,
                                                 @RequestParam("file") MultipartFile file, @RequestParam("comment") String comment){
        String realPath = "/var/www/html/never_pictures/proofs/";
        String dbPath = "http://vrot.bounceme.net:8081/never_pictures/proofs/";
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        String pathEnding = user.get().getUsername() + "_" + questId + ".jpg";

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(realPath + pathEnding)));
                stream.write(bytes);
                stream.close();
                proofController.addProof(questId, user.get(), dbPath + pathEnding, comment);
                return gson.toJson(new Answer(Constants.SUCCEED));
            } catch (Exception e) {
                return gson.toJson(new Answer(Constants.FAILED));
            }
        } else {
            return gson.toJson(new Answer(Constants.FILE_IS_EMPTY));
        }
    }

    @RequestMapping(value="/uploadAvatar", method=RequestMethod.POST)
    public @ResponseBody String handleUserAvatarUpload(@RequestParam("token") String token, @RequestParam("file") MultipartFile file){
        String realPath = "/var/www/html/never_pictures/user_avatars/";
        String dbPath = "http://vrot.bounceme.net:8081/never_pictures/user_avatars/";
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        String pathEnding = user.get().getUsername() + ".jpg";

        return writeFile(file, realPath + pathEnding, user.get().getId(), dbPath + pathEnding);
        }

    @RequestMapping(value="/uploadGroupAvatar", method=RequestMethod.POST)
    public @ResponseBody String handleGroupAvatarUpload(@RequestParam("token") String token, @RequestParam("file") MultipartFile file, @RequestParam("gid") Long groupId){
        String realPath = "/var/www/html/never_pictures/group_avatars/";
        String dbPath = "http://vrot.bounceme.net:8081/never_pictures/group_avatars/";
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new Answer(Constants.FAILED));
        }
        String pathEnding = user.get().getUsername() + ".jpg";
        return writeFile(file, realPath + pathEnding, user.get().getId(), dbPath + pathEnding);
    }

    private String writeFile(MultipartFile file, String realPath, Long userId, String dbPath) {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(realPath)));
                stream.write(bytes);
                stream.close();
                groupController.setAvatar(userId, dbPath);
                return gson.toJson(new Answer(Constants.SUCCEED));
            } catch (Exception e) {
                return gson.toJson(new Answer(Constants.FAILED));
            }
        } else {
            return gson.toJson(new Answer(Constants.FILE_IS_EMPTY));
        }
    }
}