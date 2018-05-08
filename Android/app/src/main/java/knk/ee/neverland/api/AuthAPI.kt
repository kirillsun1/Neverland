package knk.ee.neverland.api

import knk.ee.neverland.api.models.RegistrationData
import knk.ee.neverland.exceptions.APIException

interface AuthAPI {
    @Throws(APIException::class)
    fun attemptLogin(login: String, password: String): Pair<String, Int>

    @Throws(APIException::class)
    fun registerAccount(registrationData: RegistrationData): Pair<String, Int>

    fun getUserID(token: String): Int
}
