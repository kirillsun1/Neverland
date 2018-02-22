package knk.ee.neverland.fakeapi;

public final class FakeAPISingleton {
    private static final FakeAuthAPI FAKE_AUTH_API = new FakeAuthAPI();

    public static FakeAuthAPI getAuthInstance() {
        return FAKE_AUTH_API;
    }


}
