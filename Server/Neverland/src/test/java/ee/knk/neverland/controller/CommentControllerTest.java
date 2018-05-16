package ee.knk.neverland.controller;

import ee.knk.neverland.NeverlandApplication;
import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.CommentRepository;
import ee.knk.neverland.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CommentControllerTest {
    @Mock
    private Comment comment;
    private CommentController commentController;
    @Mock
    private TokenController tokenController;
    @Mock
    private ProofController proofController;
    @Mock
    private CommentService commentService;
    @Mock
    private User author;
    @Mock
    private Proof proof;
    private final String token = "dummy";

    public CommentControllerTest() {
    }

    @Before
    public void before() {
        this.commentController = new CommentController(tokenController, commentService, proofController);
        Optional<User> userOp = Optional.of(author);
        Optional<Proof> proofOp = Optional.of(proof);
        when(tokenController.getTokenUser(token)).thenReturn(userOp);
        when(proofController.getProofById(0L)).thenReturn(proofOp);
    }

    @Test
    public void testIfAddCommentControlsToken() {
        commentController.addComment(token, 0L, "test");
        Mockito.verify(tokenController).getTokenUser(token);
    }

    @Test
    public void testIfAddCommentGetsProof() {
        commentController.addComment(token, 0L, "test");
        Mockito.verify(proofController).getProofById(0L);
    }

    @Test
    public void testIfEditControlsToken() {
        when(commentService.getCommentById(0L)).thenReturn(Optional.of(comment));
        when(comment.getAuthor()).thenReturn(author);
        commentController.editComment(token, 0L, "test");
        Mockito.verify(tokenController).getTokenUser(token);
    }

    @Test
    public void testIfEditAsksServiceForComment() {
        when(commentService.getCommentById(0L)).thenReturn(Optional.of(comment));
        when(comment.getAuthor()).thenReturn(author);
        commentController.editComment(token, 0L, "test");
        Mockito.verify(commentService).getCommentById(0L);
    }

    @Test
    public void testIfEditAsksServiceToEdit() {
        when(commentService.getCommentById(0L)).thenReturn(Optional.of(comment));
        when(comment.getAuthor()).thenReturn(author);
        commentController.editComment(token, 0L, "test");
        Mockito.verify(commentService).editComment("test", 0L);
    }

    @Test
    public void testIfEditChecksRights() {
        when(commentService.getCommentById(0L)).thenReturn(Optional.of(comment));
        when(comment.getAuthor()).thenReturn(author);
        commentController.editComment(token, 0L, "test");
        Mockito.verify(comment).getAuthor();
    }

    @Test
    public void testIfGetProofsCommentsControlsToken() {
        commentController.getProofComments(token, 0L);
        Mockito.verify(tokenController).getTokenUser(token);
    }

    @Test
    public void testIfGetProofsCommentsTakesProof() {
        commentController.getProofComments(token, 0L);
        Mockito.verify(proofController).getProofById(0L);
    }

    @Test
    public void testIfGetProofsCommentsAsksServiceForComments() {
        when(commentService.getProofComments(proof)).thenReturn(new ArrayList<>());
        commentController.getProofComments(token, 0L);
        Mockito.verify(commentService).getProofComments(proof);
    }

}