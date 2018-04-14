package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentPojo implements Pojo {
    private Long id;
    @SerializedName("text")
    private String text;
    @SerializedName("author")
    private UserPojo author;
    @SerializedName("creation_time")
    private LocalDateTime createTime;

    public static class CommentPojoBuilder {
        private CommentPojo commentPojo = new CommentPojo();

        public CommentPojoBuilder setId(Long id) {
            commentPojo.id = id;
            return this;
        }

        public CommentPojoBuilder setText(String text) {
            commentPojo.text = text;
            return this;
        }

        public CommentPojoBuilder setAuthor(UserPojo author) {
            commentPojo.author = author;
            return this;
        }

        public CommentPojoBuilder setCreateTime(LocalDateTime time) {
            commentPojo.createTime = time;
            return this;
        }

        public CommentPojo getCommentPojo() {
            return commentPojo;
        }
    }
}
