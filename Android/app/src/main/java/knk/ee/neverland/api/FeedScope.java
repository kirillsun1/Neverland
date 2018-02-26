package knk.ee.neverland.api;

public enum FeedScope {
    WORLD(0x1), GROUPS(0x2), FOLLOWING(0x3);

    private final int value;

    FeedScope(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
