package ee.knk.neverland.answer.pojo.builder;

import ee.knk.neverland.answer.pojo.CommentPojo;
import ee.knk.neverland.answer.pojo.UserPojo;

import java.time.LocalDateTime;

public class CommentPojoBuilder {
    private CommentPojo commentPojo = new CommentPojo();

    public CommentPojoBuilder withId(Long id) {
        commentPojo.setId(id);
        return this;
    }

    public CommentPojoBuilder withText(String text) {
        commentPojo.setText(text);
        return this;
    }

    public CommentPojoBuilder withAuthor(UserPojo author) {
        commentPojo.setAuthor(author);
        return this;
    }

    public CommentPojoBuilder withCreateTime(LocalDateTime time) {
        commentPojo.setCreateTime(time);
        return this;
    }

    public CommentPojo getCommentPojo() {
        return commentPojo;
    }
}