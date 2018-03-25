package knk.ee.neverland.api

import knk.ee.neverland.api.neverlandapi.NeverlandAuthAPI
import knk.ee.neverland.api.neverlandapi.NeverlandProofAPI
import knk.ee.neverland.api.neverlandapi.NeverlandQuestAPI
import knk.ee.neverland.api.neverlandapi.ProofAPI

object DefaultAPI {
    var userToken: String? = null
        private set
    var userLogin: String? = null
        private set

    val authAPI: AuthAPI = NeverlandAuthAPI()
    val questAPI: QuestApi = NeverlandQuestAPI()
    val proofAPI: ProofAPI = NeverlandProofAPI()

    fun isKeySet(): Boolean = userToken != null && userLogin != null

    fun setUserData(login: String, token: String) {
        if (!token.isEmpty() && !login.isEmpty()) {
            userToken = token
            userLogin = login
        }
    }

    private fun writeTokenToAPIs() {
        questAPI.token = userToken!!
        proofAPI.token = userToken!!
    }
}
