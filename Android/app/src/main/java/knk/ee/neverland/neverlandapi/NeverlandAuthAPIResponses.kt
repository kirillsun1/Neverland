package knk.ee.neverland.neverlandapi

class NeverlandAuthAPIResponses {
    data class IsKeyActiveResponse(
            val token: String,
            val isActive: Boolean
    )

    data class RegistrationResponse(
            val code: Int,
            val token: String
    )

    data class AttemptLoginResponse(
            val code: Int,
            val token: String
    )
}