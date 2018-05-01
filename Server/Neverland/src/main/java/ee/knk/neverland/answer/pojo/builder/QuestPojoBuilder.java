package ee.knk.neverland.answer.pojo.builder;

import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.UserPojo;

import java.time.LocalDateTime;

public class QuestPojoBuilder {
    private QuestPojo questPojo = new QuestPojo();

    public QuestPojoBuilder withId(Long id) {
        questPojo.setId(id);
        return this;
    }

    public QuestPojoBuilder withTitle(String title) {
        questPojo.setTitle(title);
        return this;
    }

    public QuestPojoBuilder withDescription(String description) {
        questPojo.setDescription(description);
        return this;
    }

    public QuestPojoBuilder withAuthor(UserPojo author) {
        questPojo.setAuthor(author);
        return this;
    }

    public QuestPojoBuilder withAddingTime(LocalDateTime time) {
        questPojo.setAddingTime(time);
        return this;
    }

    public QuestPojoBuilder withTakenTime(LocalDateTime time) {
        questPojo.setTakenTime(time);
        return this;
    }

    public QuestPojo getQuestPojo() {
        return questPojo;
    }
}
