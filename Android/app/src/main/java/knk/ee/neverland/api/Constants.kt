package knk.ee.neverland.api

object Constants {
    const val FAILED = -1
    const val NETWORK_ERROR = -100
    const val BAD_REQUEST_TO_API = -101
    const val NO_TOKEN_SET = -102

    const val SUCCESS = 1

    val LOGIN_REGEX = "^[a-z0-9_-]{6,16}\$".toRegex()
    val PASSWORD_REGEX = "^[a-z0-9_-]{6,18}\$".toRegex()
    val EMAIL_REGEX = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})\$".toRegex()
    val NAME_REGEX = "^[A-Za-z ,.'-]+\$".toRegex()

    val QUEST_NAME_MINIMUM_SYMBOLS: Int = 5
    val QUEST_DESC_MINIMUM_SYMBOLS: Int = 5
    val QUEST_NAME_MAXIMUM_SYMBOLS: Int = 30
    val QUEST_DESC_MAXIMUM_SYMBOLS: Int = 480

    val TIME_FORMAT = "dd.MM.yyyy"
}