package knk.ee.neverland.api.neverlandapi

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

    data class GetQuestAPIResponse(
        val code: Int,
        val quest: Quest
    )
}