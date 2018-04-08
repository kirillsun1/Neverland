package ee.knk.neverland.repository;

import ee.knk.neverland.entity.Comment;
import ee.knk.neverland.entity.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT comment FROM Comment comment WHERE comment.id = :id")
    Optional<Comment> findOneIfExists(@Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Comment comment SET comment.text = :text WHERE comment.id = :id")
    void editComment(@Param("text") String text, @Param("id") Long id);

    @Query("SELECT comment FROM Comment comment WHERE comment.proof = :proof ORDER BY comment.id DESC")
    List<Comment> getProofComments(@Param("proof") Proof proof);
}
