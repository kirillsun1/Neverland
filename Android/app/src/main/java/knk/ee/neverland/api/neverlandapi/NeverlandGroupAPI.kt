package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.GroupAPI
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.models.Group
import knk.ee.neverland.models.User
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class NeverlandGroupAPI(private val gson: Gson) : GroupAPI {
    override var token: String = ""

    override fun createGroup(groupName: String) {
        val link = URLLinkBuilder(API_LINK, "createGroup")
            .addParam("token", token)
            .addParam("g_name", groupName)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }

    override fun getMyGroups(): List<Group> {
        val link = URLLinkBuilder(API_LINK, "getMyGroups")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetGroupsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.groups
    }

    override fun getGroup(groupID: Int): Group {
        val link = URLLinkBuilder(API_LINK, "getGroupInfo")
            .addParam("token", token)
            .addParam("gid", groupID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetGroupAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.group
    }

    override fun uploadAvatar(groupID: Int, avatar: File) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", token)
            .addFormDataPart(
                "file", avatar.name,
                RequestBody.create(MediaType.parse("image"), avatar)
            )
            .addFormDataPart("gid", groupID.toString())
            .build()

        val link = URLLinkBuilder(API_LINK, "uploadGroupAvatar")
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

    override fun subscribe(groupID: Int) {
        val link = URLLinkBuilder(API_LINK, "subscribe")
            .addParam("token", token)
            .addParam("gid", groupID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }

    override fun unsubscribe(groupID: Int) {
        val link = URLLinkBuilder(API_LINK, "unsubscribe")
            .addParam("token", token)
            .addParam("gid", groupID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }

    override fun getGroupsToJoin(): List<Group> {
        val link = URLLinkBuilder(API_LINK, "getNewGroups")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetGroupsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.groups
    }

    override fun getSubscribers(groupID: Int): List<User> {
        val link = URLLinkBuilder(API_LINK, "/getGroupSubscribers")
            .addParam("token", token)
            .addParam("gid", groupID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetUsersAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.users
    }
}