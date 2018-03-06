package knk.ee.neverland.api.neverlandapi

import com.google.gson.Gson
import knk.ee.neverland.api.Constants
import knk.ee.neverland.api.QuestApi
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Quest
import knk.ee.neverland.network.NetworkGetConnection
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
        // val senddata = NeverlandAPIResponses.SubmitQuestAPIRequest(token, quest.title, quest.description, quest.groupID)

        val data = NetworkGetConnection(API_LINK)
                .setAction("submitquest")
                .addParam("token", token)
                .addParam("title", quest.title)
                .addParam("desc", quest.description)
                .addParam("gid", quest.groupID.toString())
                // .addText(gson.toJson(senddata))
                .onFailed(standardOnFailed)
                .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.SimpleQuestAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }
    }

    override fun submitProof(proof: Proof) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeQuest(id: Int) {
        val data = NetworkGetConnection(API_LINK)
                .setAction("takequest")
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
                .setAction("dropquest")
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
                .setAction("getquest")
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
                .setAction("getmyquests")
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
                .setAction("getquests")
                .addParam("token", token)
                .onFailed(standardOnFailed)
                .getContent()

        val response = Gson().fromJson(data, NeverlandAPIResponses.GetQuestsAPIResponse::class.java)

        if (response.code != Constants.SUCCESS) {
            throw QuestAPIException(response.code)
        }

        // return makeQuestsFromResponseQuests(response.quests)
        return response.quests
    }

/*
    private fun makeQuestsFromResponseQuests(listOfResponseQuests: List<NeverlandAPIResponses.ResponseQuest>): List<Quest> {
        val questsList = mutableListOf<Quest>()

        listOfResponseQuests.forEach {

        }

        return questsList
    }*/

/*    private fun convertResponseQuestToQuest(rQuest: NeverlandAPIResponses.ResponseQuest): Quest {
        val user = User()
        user.login = rQuest.author.userName
        user.firstName = rQuest.author.firstName
        user.secondName = rQuest.author.secondName

        val quest = Quest()
        quest.creator = user
        quest.title = rQuest.title
        quest.description = rQuest.desc

        return quest
    }*/
}