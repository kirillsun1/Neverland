package knk.ee.neverland.api;

import knk.ee.neverland.fakeapi.FakeAuthAPI;

public class DefaultAPI {
    private static String apiLogin;
    private static String apiKey;

    // default api-s
    private static final AuthAPI AUTH_API = new FakeAuthAPI();
    private static final QuestApi QUEST_API = null;

    public static AuthAPI getAuthAPIInstance() {
        return AUTH_API;
    }

    public static QuestApi getQuestAPIInstance() {
        return QUEST_API;
    }

    public static void setUserData(String login, String key) {
        if (!login.isEmpty() && !key.isEmpty() && AUTH_API.isKeyActive(key)) {
            apiLogin = login;
            apiKey = key;
        }
    }

    public static boolean isKeySet() {
        return apiLogin != null && apiKey != null;
    }
}
