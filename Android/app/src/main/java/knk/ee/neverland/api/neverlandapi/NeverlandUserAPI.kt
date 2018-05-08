package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.UserAPI
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.models.User
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class NeverlandUserAPI(private val gson: Gson) : UserAPI {
    override var token: String = ""

    override fun getMyData(): User {
        val link = URLLinkBuilder(API_LINK, "getMyInfo")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        // TODO: ask to change api response
        val responseObject = gson.fromJson(responseBody, User::class.java)

        return responseObject
    }

    override fun getUserData(userID: Int): User {
        val link = URLLinkBuilder(API_LINK, "getUsersInfo")
            .addParam("token", token)
            .addParam("uid", userID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        // TODO: ask to change api response
        val responseObject = gson.fromJson(responseBody, User::class.java)

        return responseObject
    }

    override fun uploadAvatar(file: File) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", token)
            .addFormDataPart(
                "file", file.name,
                RequestBody.create(MediaType.parse("image"), file)
            )
            .build()

        val link = URLLinkBuilder(API_LINK, "uploadAvatar")
            .finish()

        val responseBody = NetworkRequester.makePostRequestAndGetResponseBody(link, requestBody)

        val responseObj = gson.fromJson(
            responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java
        )

        if (responseObj.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObj.code)
        }
    }
}