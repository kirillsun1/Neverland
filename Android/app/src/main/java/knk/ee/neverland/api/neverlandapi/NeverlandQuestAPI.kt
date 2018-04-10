package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.QuestAPI
import knk.ee.neverland.api.models.QuestToSubmit
import knk.ee.neverland.exceptions.APIException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.NetworkRequester
import knk.ee.neverland.network.URLLinkBuilder
import knk.ee.neverland.utils.Constants

class NeverlandQuestAPI : QuestAPI {
    override var token: String = ""

    private val API_LINK = "http://vrot.bounceme.net:8080"

    override fun submitNewQuest(quest: QuestToSubmit) {
        val link = URLLinkBuilder(API_LINK, "submitQuest")
            .addParam("token", token)
            .addParam("title", quest.title)
            .addParam("desc", quest.description)
            .addParam("gid", quest.groupID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }

    override fun takeQuest(id: Int) {
        val link = URLLinkBuilder(API_LINK, "takeQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }
    }

    override fun dropQuest(id: Int) {
        val link = URLLinkBuilder(API_LINK, "dropQuest")
            .addParam("token", token)
            .addParam("qid", id.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.SimpleResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
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
            throw APIException(responseObject.code)
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
            throw APIException(responseObject.code)
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
            throw APIException(responseObject.code)
        }

        return responseObject.quests
    }

    override fun getSuggestedByUserQuests(userID: Int): List<Quest> {
        val link = URLLinkBuilder(API_LINK, "getSuggestedQuests")
            .addParam("token", token)
            .addParam("uid", userID.toString())
            .finish()

        val responseBody = NetworkRequester.makeGetRequestAndGetResponseBody(link)

        val responseObject = Gson().fromJson(responseBody,
            NeverlandAPIResponses.GetQuestsAPIResponse::class.java)

        if (responseObject.code != Constants.SUCCESS_CODE) {
            throw APIException(responseObject.code)
        }

        return responseObject.quests
    }
}
