package knk.ee.neverland.api

object AuthAPIConstants {
    const val FAILED = -1
    const val NETWORK_ERROR = -100
    const val BAD_REQUEST_TO_API = -101

    const val SUCCESS = 1

    val LOGIN_REGEX = "^[a-z0-9_-]{6,16}\$".toRegex()
    val PASSWORD_REGEX = "^[a-z0-9_-]{6,18}\$".toRegex()
    val NAME_REGEX = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})\$".toRegex()
    val EMAIL_REGEX = "^[A-Za-z ,.'-]+\$".toRegex()
}