package knk.ee.neverland.feed;

public class FeedElement {
    // TODO: FIX DUMMY CODE

    private static int id = 0;

    private String userName;
    private String questName;

    public FeedElement() {
        id++;
        userName = "user" + id;
        questName = "questname" + id;
    }

    String getUserName() {
        return userName;
    }

    String getQuestName() {
        return questName;
    }
}
