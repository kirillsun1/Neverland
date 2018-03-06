package knk.ee.neverland.api.neverlandapi

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.models.Quest

class NeverlandAPIResponses {
    data class IsKeyActiveResponse(
            val code: Int
    )

    data class RegistrationResponse(
            val code: Int,
            val token: String
    )

    data class AttemptLoginResponse(
            val code: Int,
            val token: String
    )

    data class SimpleQuestAPIResponse(
            val code: Int
    )

    data class GetQuestsAPIResponse(
            val code: Int,
            val quests: List<Quest>
    )

/*    data class ResponseQuest(
            val id: Int,
            val title: String,
            val desc: String,
            @SerializedName("author")
            val author: ResponseUser
            // TODO: Time
    )

    data class ResponseUser(
            @SerializedName("user_name")
            val userName: String,
            @SerializedName("first_name")
            val firstName: String,
            @SerializedName("second_name")
            val secondName: String
    )*/

    data class GetQuestAPIResponse(
            val code: Int,
            val quest: Quest
    )
}