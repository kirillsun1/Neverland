package knk.ee.neverland.questview;

public class MyQuestElement {
    private String name;
    private String text;

    public MyQuestElement(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getQuestName() {
        return name;
    }

    public String getQuestDescription() {
        return text;
    }
}
