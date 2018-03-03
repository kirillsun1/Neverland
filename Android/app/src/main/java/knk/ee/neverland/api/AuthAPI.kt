package knk.ee.neverland.api

import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.pojos.RegistrationData

interface AuthAPI {
    @Throws(AuthAPIException::class)
    fun attemptLogin(login: String, password: String): String

    @Throws(AuthAPIException::class)
    fun registerAccount(registrationData: RegistrationData)

    fun isKeyActive(key: String): Boolean
}
