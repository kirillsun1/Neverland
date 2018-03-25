package ee.knk.neverland.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
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
    private Gson gson = new Gson();

    @Autowired
    public FileUploadController(TokenService tokenService, ProofController proofController) {
        this.tokenController = new TokenController(tokenService);
        this.proofController = proofController;
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("token") String token, @RequestParam("qid") Long questId,
                                                 @RequestParam("file") MultipartFile file, @RequestParam("comment") String comment){
        String standardPath = "/var/www/html/never_pictures/proofs/";
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        String path = standardPath + user.get().getUsername() + "_" + questId + ".jpg";
        proofController.addProof(questId, user.get(), path, comment);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(path)));
                stream.write(bytes);
                stream.close();
                return gson.toJson(new StandardAnswer(Constants.SUCCEED));
            } catch (Exception e) {
                return gson.toJson(new StandardAnswer(Constants.FAILED));
            }
        } else {
            return gson.toJson(new StandardAnswer(Constants.FILE_IS_EMPTY));
        }
    }
}