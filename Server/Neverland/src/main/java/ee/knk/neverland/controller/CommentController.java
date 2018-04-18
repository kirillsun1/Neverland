package ee.knk.neverland.controller;

import com.google.gson.Gson;
import ee.knk.neverland.answer.StandardAnswer;
import ee.knk.neverland.answer.ListAnswer;
import ee.knk.neverland.constants.Constants;
import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.service.CommentService;
import ee.knk.neverland.tools.CommentPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    private final TokenController tokenController;
    private final CommentService commentService;
    private final ProofController proofController;
    private final Gson gson = new Gson();

    @Autowired
    public CommentController(TokenController tokenController, CommentService commentService, ProofController proofController) {
        this.tokenController = tokenController;
        this.commentService = commentService;
        this.proofController = proofController;
    }

    @RequestMapping(value = "/addComment")
    public String addComment(@RequestParam(name = "token") String token, @RequestParam(value = "pid") Long proofId,
                             @Param(value = "text") String text) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<Proof> proof = proofController.getProofById(proofId);
        if (!proof.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        Comment comment = new Comment(user.get(), proof.get(), text);
        commentService.addComment(comment);
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));

    }

    @RequestMapping(value = "/editComment")
    public String editComment(@RequestParam(name = "token") String token, @RequestParam(value = "cid") Long commentId,
                              @RequestParam(value = "text") String text) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<Comment> comment = getCommentById(commentId);
        if (!comment.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        if (!comment.get().getAuthor().equals(user.get())) {
            return gson.toJson(new StandardAnswer(Constants.PERMISSION_DENIED));
        }
        commentService.editComment(text, commentId);
        return gson.toJson(new StandardAnswer(Constants.SUCCEED));

    }

    @RequestMapping(value = "/getProofComments")
    public String getProofComments(@RequestParam(name = "token") String token, @RequestParam(value = "pid") Long proofId) {
        Optional<User> user = tokenController.getTokenUser(token);
        if (!user.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.FAILED));
        }
        Optional<Proof> proof = proofController.getProofById(proofId);
        if (!proof.isPresent()) {
            return gson.toJson(new StandardAnswer(Constants.ELEMENT_DOES_NOT_EXIST));
        }
        List<Comment> comments = commentService.getProofComments(proof.get());
        CommentPacker packer = new CommentPacker();
        return gson.toJson(new ListAnswer(packer.packAllComments(comments)));
    }

    private Optional<Comment> getCommentById(Long id) {
        return commentService.getCommentById(id);
    }
}
