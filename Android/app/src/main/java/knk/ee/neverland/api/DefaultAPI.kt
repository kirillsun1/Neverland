package knk.ee.neverland.api

import knk.ee.neverland.api.neverlandapi.NeverlandAuthAPI
import knk.ee.neverland.api.neverlandapi.NeverlandQuestAPI

object DefaultAPI {
    var userToken: String? = null
        private set
    var userLogin: String? = null
        private set

    val authAPI: AuthAPI = NeverlandAuthAPI()
    val questAPI: QuestApi = NeverlandQuestAPI()

    fun isKeySet(): Boolean = userToken != null && userLogin != null

    fun setUserData(login: String, token: String) {
        if (!token.isEmpty() && !login.isEmpty()) {
            userToken = token
            userLogin = login

            questAPI.token = userToken!!
        }
    }
}
