package knk.ee.neverland.fakeapi

import com.google.common.hash.Hashing
import knk.ee.neverland.api.AuthAPI
import knk.ee.neverland.api.AuthAPIResponse
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.pojos.RegistrationData
import java.nio.charset.StandardCharsets
import java.util.*

class FakeAuthAPI : AuthAPI {
    private val registeredUsers = ArrayList<User>()
    private val workingKeys = ArrayList<String>()

    init {
        workingKeys.add("ABC")
    }

    @Throws(AuthAPIException::class)
    override fun attemptLogin(login: String, password: String): String {
        for (user in registeredUsers) {
            if (user.login == login && user.password == password) {
                val key = generateKey(user)
                workingKeys.add(key)
                return key
            }
        }

        throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
    }

    @Throws(AuthAPIException::class)
    override fun registerAccount(registrationData: RegistrationData) {
        if (!loginIsCorrect(registrationData.login)) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
        }

        if (!passwordIsCorrect(registrationData.password)) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
        }

        if (!nameIsCorrect(registrationData.firstName)) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
        }

        if (!nameIsCorrect(registrationData.secondName)) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
        }

        if (!emailIsCorrect(registrationData.email)) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
        }

        val user = User()

        user.login = registrationData.login
        user.password = registrationData.password
        user.firstName = registrationData.firstName
        user.secondName = registrationData.secondName
        user.email = registrationData.email

        registeredUsers.add(user)
    }

    override fun isKeyActive(key: String): Boolean {
        return workingKeys.contains(key)
    }

    private fun generateKey(user: User): String {
        val line = String.format("[%s==%s]", user.login, user.password)
        return Hashing.sha256()
                .hashString(line, StandardCharsets.UTF_8)
                .toString()
    }

    private fun loginIsCorrect(login: String): Boolean {
        return login.matches("^[a-z0-9_-]{6,16}$".toRegex())
    }

    private fun passwordIsCorrect(password: String): Boolean {
        return password.matches("^[a-z0-9_-]{6,18}$".toRegex())
    }

    private fun emailIsCorrect(email: String): Boolean {
        return email.matches("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$".toRegex())
    }

    private fun nameIsCorrect(name: String): Boolean {
        return name.matches("^[A-Za-z ,.'-]+$".toRegex())
    }

    private inner class User {
        internal var login: String? = null
        internal var password: String? = null
        internal var email: String? = null
        internal var firstName: String? = null
        internal var secondName: String? = null
    }
}
