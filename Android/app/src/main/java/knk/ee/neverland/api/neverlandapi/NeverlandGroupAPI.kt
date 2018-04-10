package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.GroupAPI
import knk.ee.neverland.api.models.GroupToCreate
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.models.Group
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants

class NeverlandGroupAPI : GroupAPI {
    override var token: String = ""

    override fun createGroup(group: GroupToCreate) {
        val link = URLLinkBuilder(API_LINK, "createGroup")
            .addParam("token", token)
            .addParam("g_name", group.groupName)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
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

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.GetGroupsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.groups
    }
}