package knk.ee.neverland.api.neverlandapi

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.models.Group
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Quest
import knk.ee.neverland.models.Rating
import knk.ee.neverland.models.User

class NeverlandAPIResponses {
    data class GetUsersAPIResponse(
        val code: Int,
        @SerializedName("elements")
        val users: List<User>
    )

    data class IsKeyActiveResponse(
        val code: Int,
        @SerializedName("uid")
        val userID: Int
    )

    data class RegistrationResponse(
        val code: Int,
        val token: String,
        @SerializedName("uid")
        val userID: Int
    )

    data class AttemptLoginResponse(
        val code: Int,
        val token: String,
        @SerializedName("uid")
        val id: Int?
    )

    data class SimpleResponse(
        val code: Int
    )

    data class GetQuestsAPIResponse(
        val code: Int,
        @SerializedName("elements")
        val quests: List<Quest>
    )

    data class GetQuestAPIResponse(
        val code: Int,
        val quest: Quest
    )

    data class GetProofsAPIResponse(
        val code: Int,
        @SerializedName("elements")
        val proofs: List<Proof>
    )

    data class GetGroupsAPIResponse(
        val code: Int,
        @SerializedName("elements")
        val groups: List<Group>
    )

    data class GetGroupAPIResponse(
        val code: Int,
        @SerializedName("body")
        val group: Group
    )

    data class VoteAPIResponse(
        val code: Int,
        @SerializedName("body")
        val rating: Rating
    )
}
