package ee.knk.neverland.answer.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestPojo implements Pojo {
    private Long id;
    private String title;
    @SerializedName("desc")
    private String description;
    @SerializedName("author")
    private UserPojo author;
    @SerializedName("time_created")
    private LocalDateTime addingTime;
    @SerializedName("time_taken")
    private LocalDateTime takenTime;

    public void setTakenTime(LocalDateTime takenTime) {
        this.takenTime = takenTime;
    }

    public static class QuestPojoBuilder {
        private QuestPojo questPojo = new QuestPojo();

        public QuestPojoBuilder setId(Long id) {
            questPojo.id = id;
            return this;
        }

        public QuestPojoBuilder setTitle(String title) {
            questPojo.title = title;
            return this;
        }

        public QuestPojoBuilder setDescription(String description) {
            questPojo.description = description;
            return this;
        }

        public QuestPojoBuilder setAuthor(UserPojo author) {
            questPojo.author = author;
            return this;
        }

        public QuestPojoBuilder setAddingTime(LocalDateTime time) {
            questPojo.addingTime = time;
            return this;
        }

        public QuestPojoBuilder setTakenTime(LocalDateTime time) {
            questPojo.takenTime = time;
            return this;
        }

        public QuestPojo getQuestPojo() {
            return questPojo;
        }
    }
}
