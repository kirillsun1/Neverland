package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.entity.Quest;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class CommentServiceImplTest {

    @Autowired
    CommentRepository commentRepository;
    private CommentServiceImpl commentService;

    private User user = new User("dummy", "dummy", "dummy", "dummy", "dummy");
    private Proof proof = new Proof(user, new Quest(), "dummy", "dummy");


    @Before
    public void before() {
        when(proof.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        commentRepository.deleteAll();
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    public void addComment() {
        Comment comment = new Comment(user, proof, "dummy");
        commentService.addComment(comment);
        assert(1 == commentService.getProofComments(proof).size());
    }

    @Test
    public void getCommentById() {
    }

    @Test
    public void editComment() {
    }

    @Test
    public void getProofComments() {
    }
}