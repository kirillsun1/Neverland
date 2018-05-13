package knk.ee.neverland.utils

object Constants {
    const val FAIL_CODE = -1
    const val SUCCESS_CODE = 1

    const val QUEST_ALREADY_TAKEN_CODE = -2

    const val NETWORK_ERROR_CODE = -100
    const val BAD_REQUEST_TO_API_CODE = -101
    const val NETWORK_TIMEOUT: Int = -102

    const val SUBMITTING_PROOF_REQUEST_CODE = 1
    const val SUBMIT_NEW_QUEST_REQUEST_CODE: Int = 22

    const val ELEMENT_NUMBER_TO_START_RECYCLING_FROM = 20

    val LOGIN_REGEX = "(?!^[0-9]*\$)^([-_a-zA-Z0-9]{6,16})\$".toRegex()
    val PASSWORD_REGEX = "^[a-zA-Z0-9_-]{6,18}\$".toRegex()
    val EMAIL_REGEX = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$)".toRegex()
    val NAME_REGEX = "^[A-Za-z ,.'-]+\$".toRegex()

    const val DATE_TIME_FORMAT: String = "yyyy-MM-dd HH:mm"

    const val QUEST_NAME_MINIMUM_SYMBOLS: Int = 5
    const val QUEST_DESC_MINIMUM_SYMBOLS: Int = 5
    const val QUEST_NAME_MAXIMUM_SYMBOLS: Int = 30
    const val QUEST_DESC_MAXIMUM_SYMBOLS: Int = 480

    const val POSITIVE_VOTE = 1
    const val NEGATIVE_VOTE = -1
    const val NO_VOTE = 0
}