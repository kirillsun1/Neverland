package knk.ee.neverland.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import knk.ee.neverland.api.neverlandapi.NeverlandAuthAPI
import knk.ee.neverland.api.neverlandapi.NeverlandGroupAPI
import knk.ee.neverland.api.neverlandapi.NeverlandProofAPI
import knk.ee.neverland.api.neverlandapi.NeverlandQuestAPI
import knk.ee.neverland.api.neverlandapi.NeverlandUserAPI
import knk.ee.neverland.api.neverlandapi.NeverlandVoteAPI
import knk.ee.neverland.utils.Constants
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

object DefaultAPI {
    var userToken: String? = null
        private set
    var userLogin: String? = null
        private set
    var userID: Int? = null


    val authAPI: AuthAPI
    val questAPI: QuestAPI
    val proofAPI: ProofAPI
    val userAPI: UserAPI
    val voteAPI: VoteAPI
    val groupAPI: GroupAPI

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java,
                JsonDeserializer<LocalDateTime> { json, _, _ ->
                    LocalDateTime.parse(json!!.asString,
                        DateTimeFormat.forPattern(Constants.DATE_TIME_FORMAT))
                })
            .create()

        authAPI = NeverlandAuthAPI(gson)
        questAPI = NeverlandQuestAPI(gson)
        proofAPI = NeverlandProofAPI(gson)
        userAPI = NeverlandUserAPI(gson)
        voteAPI = NeverlandVoteAPI(gson)
        groupAPI = NeverlandGroupAPI(gson)
    }

    fun isKeySet(): Boolean = userToken != null && userLogin != null

    fun setUserData(login: String, token: String, id: Int) {
        if (!token.isEmpty() && !login.isEmpty()) {
            userToken = token
            userLogin = login
            userID = id

            writeTokenToAPIs()
        }
    }

    private fun writeTokenToAPIs() {
        questAPI.token = userToken!!
        proofAPI.token = userToken!!
        userAPI.token = userToken!!
        voteAPI.token = userToken!!
        groupAPI.token = userToken!!
    }
}
