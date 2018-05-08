package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
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

class NeverlandProofAPI(private val gson: Gson) : ProofAPI {
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

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }

    override fun getProofs(): List<Proof> {
        val link = URLLinkBuilder(API_LINK, "getAllProofs")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetProofsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.proofs
    }

    override fun getProofsByUserID(userID: Int): List<Proof> {
        val link = URLLinkBuilder(API_LINK, "getUsersProofs")
            .addParam("token", token)
            .addParam("uid", userID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetProofsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.proofs
    }

    override fun getProofsByQuestID(questID: Int): List<Proof> {
        val link = URLLinkBuilder(API_LINK, "getQuestsProofs")
            .addParam("token", token)
            .addParam("qid", questID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = gson.fromJson(responseBody,
            NeverlandAPIResponses.GetProofsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.proofs
    }

    override fun getMyProofs(): List<Proof> {
        return getProofs()
    }
}
