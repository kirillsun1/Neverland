package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.repository.CommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private Comment comment;

    @Mock
    private Proof proof;

    @Test
    public void testIfAddCommentCallsRepo() {
        when(commentRepository.saveAndFlush(any())).thenReturn(comment);
        commentService.addComment(comment);
        verify(commentRepository).saveAndFlush(comment);
    }

    @Test
    public void testIfGetCommentByIdCallsRepo() {
        when(commentRepository.findOneIfExists(0L)).thenReturn(Optional.empty());
        commentService.getCommentById(0L);
        verify(commentRepository).findOneIfExists(0L);
    }

    @Test
    public void testIfEditCommentCallsRepo() {
        commentService.editComment("", 0L);
        verify(commentRepository).editComment("", 0L);
    }

    @Test
    public void testIfGetProofCommentsCallsRepo() {
        when(commentRepository.getProofComments(proof)).thenReturn(new ArrayList<>());
        commentService.editComment("", 0L);
        verify(commentRepository).editComment("", 0L);
    }

}
