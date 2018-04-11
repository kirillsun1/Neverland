package ee.knk.neverland.answer;

import ee.knk.neverland.answer.pojo.Pojo;
import ee.knk.neverland.constants.Constants;
import lombok.Data;
import org.hibernate.jpa.criteria.expression.ConcatExpression;

import java.util.List;

@Data
public class ListAnswer {
    private List<Pojo> elements;
    private int code = Constants.SUCCEED;

    public ListAnswer(int code) {
        this.code = code;
    }

    public ListAnswer(List<Pojo> elements, int code) {
        this.elements = elements;
    }

    public ListAnswer(List<Pojo> elements) {
        this.elements = elements;
    }
}
