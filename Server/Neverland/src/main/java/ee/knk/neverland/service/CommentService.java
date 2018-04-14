package ee.knk.neverland.service;

import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment addComment(Comment comment);

    Optional<Comment> getCommentById(Long id);

    void editComment(String text, Long commentId);

    List<Comment> getProofComments(Proof proof);
}
