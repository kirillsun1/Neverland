package ee.knk.neverland.answer.pojo.builder;

import ee.knk.neverland.answer.pojo.ProofPojo;
import ee.knk.neverland.answer.pojo.QuestPojo;
import ee.knk.neverland.answer.pojo.RatingPojo;
import ee.knk.neverland.answer.pojo.UserPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProofPojoBuilder {
    private ProofPojo proofPojo = new ProofPojo();

    public ProofPojoBuilder withId(Long id) {
        proofPojo.setId(id);
        return this;
    }

    public ProofPojoBuilder withComment(String comment) {
        proofPojo.setComment(comment);
        return this;
    }

    public ProofPojoBuilder withPicturePath(String picturePath) {
        proofPojo.setPicturePath(picturePath);
        return this;
    }

    public ProofPojoBuilder withQuest(QuestPojo quest) {
        proofPojo.setQuest(quest);
        return this;
    }

    public ProofPojoBuilder withProofer(UserPojo proofer) {
        proofPojo.setProofer(proofer);
        return this;
    }

    public ProofPojoBuilder withTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        proofPojo.setTime(time.format(formatter));
        return this;
    }

    public ProofPojoBuilder withRating(int positiveRating, int  negativeRating, int myVote) {
        RatingPojo rating = new RatingPojo(positiveRating, negativeRating, myVote);
        proofPojo.setRating(rating);
        return this;
    }

    public ProofPojo getProofPojo() {
        return proofPojo;
    }
}