package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NeverlandProofAPI : ProofAPI {
    override var token: String = ""

    override fun submitProof(proof: ProofToSubmit) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", token)
            .addFormDataPart("qid", proof.questID.toString())
            .addFormDataPart("comment", proof.comment)
            .addFormDataPart("file", proof.content.name,
                RequestBody.create(MediaType.parse("image"), proof.content))
            .build()

        val link = URLLinkBuilder(API_LINK, "upload")
            .finish()

        val responseBody = NetworkRequester.makePostRequestAndGetResponseBody(link, requestBody)

        val responseObj = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (responseObj.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObj.code)
        }
    }

    override fun getProofs(feedScope: FeedScope) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}