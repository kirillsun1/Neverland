package knk.ee.neverland.api.fakeapi

import com.google.common.hash.Hashing
import knk.ee.neverland.api.AuthAPI
import knk.ee.neverland.api.Constants
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.models.RegistrationData
import java.nio.charset.StandardCharsets
import java.util.*

class FakeAuthAPI : AuthAPI {
    private val registeredUsers = ArrayList<User>()
    private val workingKeys = ArrayList<String>()

    @Throws(AuthAPIException::class)
    override fun attemptLogin(login: String, password: String): String {
        for (user in registeredUsers) {
            if (user.login == login && user.password == password) {
                return makeAndRegisterKey(user)
            }
        }

        throw AuthAPIException(Constants.FAILED)
    }

    @Throws(AuthAPIException::class)
    override fun registerAccount(registrationData: RegistrationData): String {
        if (!loginIsCorrect(registrationData.login) || !passwordIsCorrect(registrationData.password)
                || !nameIsCorrect(registrationData.firstName) || !nameIsCorrect(registrationData.secondName)
                || !emailIsCorrect(registrationData.email)) {
            throw AuthAPIException(Constants.FAILED)
        }
        val user = User()

        user.login = registrationData.login
        user.password = registrationData.password
        user.firstName = registrationData.firstName
        user.secondName = registrationData.secondName
        user.email = registrationData.email

        registeredUsers.add(user)

        return makeAndRegisterKey(user)
    }

    override fun isTokenActive(token: String): Boolean {
        return workingKeys.contains(token)
    }

    private fun generateKey(user: User): String {
        val line = String.format("[%s==%s]", user.login, user.password)
        return Hashing.sha256()
                .hashString(line, StandardCharsets.UTF_8)
                .toString()
    }

    private fun makeAndRegisterKey(user: User): String {
        val key = generateKey(user)
        workingKeys.add(key)
        return generateKey(user)
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

