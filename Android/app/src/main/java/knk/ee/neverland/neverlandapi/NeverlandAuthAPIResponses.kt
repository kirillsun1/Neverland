package knk.ee.neverland.neverlandapi

class NeverlandAuthAPIResponses {
    data class IsKeyActiveResponse(
            val key: String,
            val isActive: Boolean
    )

    data class RegistrationResponse(
            val success: Boolean,
            val errorCode: Int?
    )

    data class AttemptLoginResponse(
            val success: Boolean,
            val errorCode: Int?,
            val key: String?
    )
}