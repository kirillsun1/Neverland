package knk.ee.neverland.neverlandapi

import com.google.common.hash.Hashing
import com.google.gson.Gson
import knk.ee.neverland.api.AuthAPI
import knk.ee.neverland.api.AuthAPIResponse
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
                params = mapOf(login to "login", password to "password"))
        val response = gson.fromJson(data, NeverlandAuthAPIResponses.AttemptLoginResponse::class.java)

        if (!response.success) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED) // TODO: think about how to show errors
        }

        return response.key!!
    }

    override fun registerAccount(registrationData: RegistrationData) {
        val gson = Gson()
        val data = getDataFromConnection("register",
                params = registrationDataToMap(registrationData))
        val response = gson.fromJson(data, NeverlandAuthAPIResponses.RegistrationResponse::class.java)

        if (!response.success) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED) // TODO: think about how to show errors
        }
    }

    override fun isKeyActive(key: String): Boolean {
        val gson = Gson()
        val data = getDataFromConnection("keyactive", params = mapOf(key to "key"))
        val response = gson.fromJson(data, NeverlandAuthAPIResponses.IsKeyActiveResponse::class.java)

        return response.isActive
    }

    private fun joinParamsForLink(params: Map<String, String>): String {
        val stringBuilder = StringBuilder()
        var cur = 0
        params.forEach({
            if (cur++ > 0) stringBuilder.append(",")
            stringBuilder.append(it.key).append("=").append(it.value)
        });
        return stringBuilder.toString()
    }

    private fun getDataFromConnection(requestName: String, method: String = "GET",
                                      params: Map<String, String>): String {
        val query = String.format("%s/%s?%s", API_LINK, requestName, joinParamsForLink(params))
        val connection = URL(query).openConnection() as HttpURLConnection
        connection.requestMethod = method
        val code = connection.responseCode
        if (code != HttpURLConnection.HTTP_OK) {
            throw AuthAPIException(AuthAPIResponse.REQUEST_FAILED)
        }
        return connection.inputStream.bufferedReader().readText()
    }

    private fun registrationDataToMap(registrationData: RegistrationData): Map<String, String> {
        return mapOf(
                registrationData.login to "login",
                encodePassword(registrationData.password) to "password",
                registrationData.email to "email",
                registrationData.firstName to "firstname",
                registrationData.secondName to "secondname")
    }

    private fun encodePassword(password: String): String {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}