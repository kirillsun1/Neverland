package knk.ee.neverland.questview;

public class QuestElement {
    private String name;
    private String text;

    public QuestElement(String name, String text) {
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
