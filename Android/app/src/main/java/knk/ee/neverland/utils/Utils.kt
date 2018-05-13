package knk.ee.neverland.utils

object Utils {
    fun loginIsCorrect(login: String): Boolean = login.matches(Constants.LOGIN_REGEX)

    fun passwordIsCorrect(password: String): Boolean = password.matches(Constants.PASSWORD_REGEX)

    fun emailIsCorrect(email: String): Boolean = email.matches(Constants.EMAIL_REGEX)

    fun nameIsCorrect(name: String): Boolean = name.matches(Constants.NAME_REGEX)

    fun compactHugeNumber(number: Int): String {
        var ks = ""
        var doubleNumber = number.toDouble()

        while (number >= 1000) {
            ks += "K"
            doubleNumber /= 1000
        }

        return "${doubleNumber.toInt()}$ks"
    }
}