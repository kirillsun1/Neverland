package knk.ee.neverland.api.neverlandapi

import com.google.common.hash.Hashing
import com.google.gson.Gson
import knk.ee.neverland.api.AuthAPI
import knk.ee.neverland.api.Constants
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.models.RegistrationData
import knk.ee.neverland.network.NetworkGetConnection
import java.net.HttpURLConnection
import java.nio.charset.StandardCharsets

class NeverlandAuthAPI : AuthAPI {
    private val API_LINK = "http://vrot.bounceme.net:8080"

    private val standardOnFailed = { code: Int ->
        when (code) {
            HttpURLConnection.HTTP_NOT_FOUND ->
                throw AuthAPIException(Constants.BAD_REQUEST_TO_API)
            HttpURLConnection.HTTP_INTERNAL_ERROR ->
                throw AuthAPIException(Constants.BAD_REQUEST_TO_API)
            else -> throw AuthAPIException(Constants.NETWORK_ERROR)
        }
    }

    override fun attemptLogin(login: String, password: String): String {
        val data = NetworkGetConnection(API_LINK)
                .setAction("login")
                .addParam("username", login)
                .addParam("password", password)
                .onFailed(standardOnFailed)
                .getContent()

        val gson = Gson()
        val response = gson.fromJson(data, NeverlandAPIResponses.AttemptLoginResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw AuthAPIException(response.code)
        }

        return response.token
    }

    override fun registerAccount(registrationData: RegistrationData): String {
        val data = NetworkGetConnection(API_LINK)
                .setAction("register")
                .addParam("username", registrationData.login)
                .addParam("password", encodePassword(registrationData.password))
                .addParam("firstname", registrationData.firstName)
                .addParam("secondname", registrationData.secondName)
                .addParam("email", registrationData.email)
                .onFailed(standardOnFailed)
                .getContent()

        val gson = Gson()
        val response = gson.fromJson(data, NeverlandAPIResponses.RegistrationResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw AuthAPIException(response.code)
        }

        return response.token
    }

    override fun isTokenActive(token: String): Boolean {
        val data = NetworkGetConnection(API_LINK)
                .setAction("tokencheck")
                .addParam("token", token)
                .onFailed(standardOnFailed)
                .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.IsKeyActiveResponse::class.java)

        return response.code == Constants.SUCCESS
    }

    private fun encodePassword(password: String): String {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}