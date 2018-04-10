package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.VoteAPI
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants

class NeverlandVoteAPI : VoteAPI {
    override var token: String = ""

    override fun voteFor(proofID: Int) {
        vote(proofID, true)
    }

    override fun voteAgainst(proofID: Int) {
        vote(proofID, false)
    }

    private fun vote(proofID: Int, isFor: Boolean) {
        val link = URLLinkBuilder(API_LINK, "vote")
            .addParam("token", token)
            .addParam("pid", proofID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }
}