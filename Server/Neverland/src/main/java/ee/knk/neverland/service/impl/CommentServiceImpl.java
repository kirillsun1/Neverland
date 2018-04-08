package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;
import ee.knk.neverland.repository.CommentRepository;
import ee.knk.neverland.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findOneIfExists(id);
    }

    @Override
    public void editComment(String text, Long commentId) {
        commentRepository.editComment(text, commentId);
    }

    @Override
    public List<Comment> getProofComments(Proof proof) {
        return commentRepository.getProofComments(proof);
    }
}
