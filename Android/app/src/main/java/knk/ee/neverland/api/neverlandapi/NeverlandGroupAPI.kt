package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.GroupAPI
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.models.Group
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants

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
}