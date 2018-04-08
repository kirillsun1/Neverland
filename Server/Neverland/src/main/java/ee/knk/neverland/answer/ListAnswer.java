package ee.knk.neverland.answer;

import ee.knk.neverland.answer.pojo.Pojo;

import java.util.List;

public class ListAnswer {
    private List<Pojo> elements;
    private int code = 1;

    public ListAnswer(List<Pojo> elements, int code) {
        this.elements = elements;
        this.code = code;
    }


    public ListAnswer(List<Pojo> elements) {
        this.elements = elements;
    }
}
