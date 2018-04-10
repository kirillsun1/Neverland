package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.api.ProofAPI
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.api.neverlandapi.NeverlandAPIConstants.API_LINK
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.models.Proof
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
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObj.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObj.code)
        }
    }

    override fun getProofs(feedScope: FeedScope): List<Proof> {
        val link = URLLinkBuilder(API_LINK, "getAllProofs")
            // TODO: consider feedScope
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody, Array<Proof>::class.java)

        return responseObject.toList()
    }

    override fun getProofsByUserID(userID: Int): List<Proof> {
        val link = URLLinkBuilder(API_LINK, "getUsersProofs")
            .addParam("token", token)
            .addParam("uid", userID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody, Array<Proof>::class.java)

        return responseObject.toList()
    }
}
