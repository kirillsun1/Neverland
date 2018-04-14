package ee.knk.neverland.tools;

import ee.knk.neverland.answer.pojo.CommentPojo;

import ee.knk.neverland.answer.pojo.CommentPojo.CommentPojoBuilder;
import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.answer.pojo.UserPojo;
import ee.knk.neverland.entity.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentPacker {
    private CommentPojo packComment(Comment pointer) {
        UserPacker userPacker = new UserPacker();
        UserPojo author = userPacker.packUser(pointer.getAuthor());
        CommentPojoBuilder builder = new CommentPojoBuilder();
        return builder.setId(pointer.getId())
                .setText(pointer.getText())
                .setAuthor(author)
                .setCreateTime(pointer.getCreateTime())
                .getCommentPojo();

    }

    public List<Pojo> packAllComments(List<Comment> comments)  {
        List<Pojo> packedGroups = new ArrayList<>();
        for (Comment comment : comments) {
            packedGroups.add(packComment(comment));
        }
        return packedGroups;
    }
}
