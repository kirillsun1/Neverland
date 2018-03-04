package knk.ee.neverland.neverlandapi

import com.google.common.hash.Hashing
import com.google.gson.Gson
import knk.ee.neverland.api.AuthAPI
import knk.ee.neverland.api.AuthAPIConstants
import knk.ee.neverland.exceptions.AuthAPIException
import knk.ee.neverland.pojos.RegistrationData
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class NeverlandAuthAPI : AuthAPI {
    private val API_LINK = "http://vrot.bounceme.net:8080"

    override fun attemptLogin(login: String, password: String): String {
        val gson = Gson()
        val data = getDataFromConnection("login",
                params = mapOf("username" to login, "password" to encodePassword(password)))
        val response = gson.fromJson(data, NeverlandAuthAPIResponses.AttemptLoginResponse::class.java)

        if (response.code != AuthAPIConstants.SUCCESS) {
            throw AuthAPIException(response.code)
        }

        return response.token
    }

    override fun registerAccount(registrationData: RegistrationData): String {
        val gson = Gson()
        val data = getDataFromConnection("register",
                params = registrationDataToMap(registrationData))
        val response = gson.fromJson(data, NeverlandAuthAPIResponses.RegistrationResponse::class.java)

        if (response.code != AuthAPIConstants.SUCCESS) {
            throw AuthAPIException(response.code)
        }

        return response.token
    }

    override fun isTokenActive(token: String): Boolean {
        val gson = Gson()
        val data = getDataFromConnection("checktoken", params = mapOf("token" to token))
        val response = gson.fromJson(data, NeverlandAuthAPIResponses.IsKeyActiveResponse::class.java)

        return response.isActive
    }

    private fun joinParamsForLink(params: Map<String, String>): String {
        val stringBuilder = StringBuilder()
        var cur = 0
        params.forEach({
            if (cur++ > 0) stringBuilder.append("&")
            stringBuilder.append(it.key).append("=").append(it.value)
        });
        return stringBuilder.toString()
    }

    private fun getDataFromConnection(requestName: String, method: String = "GET",
                                      params: Map<String, String>): String {
        val query = String.format("%s/%s?%s", API_LINK, requestName, joinParamsForLink(params))
        println(query)
        val connection = URL(query).openConnection() as HttpURLConnection
        connection.requestMethod = method
        val code = connection.responseCode

        when (code) {
            HttpURLConnection.HTTP_BAD_REQUEST -> throw AuthAPIException(AuthAPIConstants.CONNECTION_FAILED)
            HttpURLConnection.HTTP_NOT_FOUND -> throw AuthAPIException(AuthAPIConstants.BAD_REQUEST_TO_API)
        }

        return connection.inputStream.bufferedReader().readText()
    }

    private fun registrationDataToMap(registrationData: RegistrationData): Map<String, String> {
        return mapOf(
                "username" to registrationData.login,
                "password" to encodePassword(registrationData.password),
                "email" to registrationData.email)
        // "firstname" to registrationData.firstName,
        // "secondname" to registrationData.secondName)
    }

    private fun encodePassword(password: String): String {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}