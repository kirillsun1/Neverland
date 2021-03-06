package knk.ee.neverland.api.neverlandapi

import com.google.common.hash.Hashing
import com.google.gson.Gson
import knk.ee.neverland.api.AuthAPI
import knk.ee.neverland.api.models.RegistrationData
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants
import java.nio.charset.StandardCharsets

class NeverlandAuthAPI(private val gson: Gson) : AuthAPI {
    override fun attemptLogin(login: String, password: String): Pair<String, Int> {
        val data = URLLinkBuilder(API_LINK, "login")
            .addParam("username", login)
            .addParam("password", encodePassword(password))
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(data)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.AttemptLoginResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return Pair(responseObject.token, responseObject.id!!)
    }

    override fun registerAccount(registrationData: RegistrationData): Pair<String, Int> {
        val data = URLLinkBuilder(API_LINK, "register")
            .addParam("username", registrationData.login)
            .addParam("password", encodePassword(registrationData.password))
            .addParam("firstname", registrationData.firstName)
            .addParam("secondname", registrationData.secondName)
            .addParam("email", registrationData.email)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(data)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.RegistrationResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return Pair(responseObject.token, responseObject.userID)
    }

    override fun getUserID(token: String): Int {
        val data = URLLinkBuilder(API_LINK, "tokenCheck")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(data)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.IsKeyActiveResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.userID
    }

    private fun encodePassword(password: String): String {
        return Hashing.sha256()
            .hashString(password, StandardCharsets.UTF_8)
            .toString();
    }
}