package knk.ee.neverland.views.feedview;

public class FeedElement {
    // TODO: FIX DUMMY CODE

    private static int id = 0;

    private String userName;
    private String questName;

    private int value;

    public FeedElement() {
        id++;
        userName = "user" + id;
        questName = "questname" + id;

        value = Math.min(id, 100);
    }

    String getUserName() {
        return userName;
    }

    String getQuestName() {
        return questName;
    }

    int getValue() {
        return value;
    }
}
