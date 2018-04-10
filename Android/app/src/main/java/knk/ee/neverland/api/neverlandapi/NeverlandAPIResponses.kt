package knk.ee.neverland.api.neverlandapi

import com.google.gson.annotations.SerializedName
import knk.ee.neverland.models.Group
import knk.ee.neverland.models.Proof
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
}
