package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.QuestApi
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.NetworkGetConnection
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.net.HttpURLConnection

class NeverlandQuestAPI : QuestApi {
    override var token: String = ""

    private val API_LINK = "http://vrot.bounceme.net:8080"

    private val standardOnFailed = { code: Int ->
        when (code) {
            HttpURLConnection.HTTP_NOT_FOUND ->
                throw QuestAPIException(Constants.BAD_REQUEST_TO_API)

            HttpURLConnection.HTTP_INTERNAL_ERROR ->
                throw QuestAPIException(Constants.BAD_REQUEST_TO_API)

            else -> throw QuestAPIException(code)
        }
    }

    override fun submitNewQuest(quest: Quest) {
        val data = NetworkGetConnection(API_LINK)
            .setAction("submitQuest")
            .addParam("token", token)
            .addParam("title", quest.title)
            .addParam("desc", quest.description)
            .addParam("gid", quest.groupID.toString())
            .onFailed(standardOnFailed)
            .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
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

        val urlString = URLLinkBuilder(API_LINK, "upload")
            .finish()

        val request = Request.Builder()
            .url(urlString)
            .post(requestBody)
            .build()

        val response = OkHttpClient().newCall(request).execute()

        if (!response.isSuccessful) {
            standardOnFailed(response.code())
        }

        val gson = Gson()
        val responseBody = response.body()!!.string()
        val responseObj = gson.fromJson(responseBody, NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (responseObj.code != Constants.SUCCESS) {
            throw QuestAPIException(responseObj.code)
        }
    }

    override fun takeQuest(id: Int) {
        val data = NetworkGetConnection(API_LINK)
            .setAction("takeQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .onFailed(standardOnFailed)
            .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }
    }

    override fun dropQuest(id: Int) {
        val data = NetworkGetConnection(API_LINK)
            .setAction("dropQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .onFailed(standardOnFailed)
            .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }
    }

    override fun getQuest(id: Int): Quest {
        val data = NetworkGetConnection(API_LINK)
            .setAction("getQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .onFailed(standardOnFailed)
            .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.GetQuestAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }

        return response.quest
    }

    override fun getMyQuests(): List<Quest> {
        val data = NetworkGetConnection(API_LINK)
            .setAction("getMyQuests")
            .addParam("token", token)
            .onFailed(standardOnFailed)
            .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.GetQuestsAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }

        return response.quests
    }

    override fun getQuestsToTake(): List<Quest> {
        val data = NetworkGetConnection(API_LINK)
            .setAction("getQuestsToTake")
            .addParam("token", token)
            .onFailed(standardOnFailed)
            .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.GetQuestsAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }

        return response.quests
    }
}