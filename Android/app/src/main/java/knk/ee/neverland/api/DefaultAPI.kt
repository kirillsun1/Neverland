package knk.ee.neverland.api

import knk.ee.neverland.fakeapi.FakeQuestAPI
import knk.ee.neverland.neverlandapi.NeverlandAuthAPI

object DefaultAPI {
    var userToken: String? = null
        private set
    var userLogin: String? = null
        private set

    val authAPI: AuthAPI = NeverlandAuthAPI()
    val questAPI: QuestApi = FakeQuestAPI()

    fun isKeySet(): Boolean = userToken != null && userLogin != null

    fun setUserData(login: String, token: String) {
        if (!token.isEmpty() && !login.isEmpty()) {
            userToken = token
            userLogin = login

            (questAPI as FakeQuestAPI).token = token
        }
    }
}
