package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.QuestApi
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NeverlandQuestAPI : QuestApi {
    override var token: String = ""

    private val API_LINK = "http://vrot.bounceme.net:8080"

    override fun submitNewQuest(quest: Quest) {
        val link = URLLinkBuilder(API_LINK, "submitQuest")
            .addParam("token", token)
            .addParam("title", quest.title)
            .addParam("desc", quest.description)
            .addParam("gid", quest.groupID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObject.code)
        }
    }

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

    override fun takeQuest(id: Int) {
        val link = URLLinkBuilder(API_LINK, "takeQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObject.code)
        }
    }

    override fun dropQuest(id: Int) {
        val link = URLLinkBuilder(API_LINK, "dropQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObject.code)
        }
    }

    override fun getQuest(id: Int): Quest {
        val link = URLLinkBuilder(API_LINK, "getQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.GetQuestAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObject.code)
        }

        return responseObject.quest
    }

    override fun getMyQuests(): List<Quest> {
        val link = URLLinkBuilder(API_LINK, "getMyQuests")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.GetQuestsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObject.code)
        }

        return responseObject.quests
    }

    override fun getQuestsToTake(): List<Quest> {
        val link = URLLinkBuilder(API_LINK, "getQuestsToTake")
            .addParam("token", token)
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.GetQuestsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw QuestAPIException(responseObject.code)
        }

        return responseObject.quests
    }
}
