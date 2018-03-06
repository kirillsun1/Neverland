/*
package knk.ee.neverland.api.fakeapi

import knk.ee.neverland.api.Constants
import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.api.QuestApi
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.models.Quest
import knk.ee.neverland.models.Proof

class FakeQuestAPI : QuestApi {
    override var token: String = ""

    private var mapOfTakenQuests: MutableMap<String, MutableList<Quest>> = mutableMapOf()

    private val listOfQuests: MutableList<Quest> = mutableListOf()
    private var uniqueID: Int = 1


    override fun submitNewQuest(quest: Quest) {
        if (!isQuestNameCorrect(quest.title) || !isQuestDescCorrect(quest.description)) {
            throw QuestAPIException(Constants.FAILED)
        }

        quest.id = uniqueID++
        quest.creator.login = token // TODO: replace with smth useful

        listOfQuests += quest
    }

    override fun submitProof(proof: Proof) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeQuest(id: Int) {
        val quest = getQuest(id)
        val curList = mapOfTakenQuests.getOrDefault(token, mutableListOf())
        if (quest in curList) {
            throw QuestAPIException(Constants.FAILED)
        }
        curList += quest
        mapOfTakenQuests[token] = curList
    }

    override fun dropQuest(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQuest(id: Int): Quest {
        return listOfQuests.filter { quest -> quest.id == id }.get(0)
    }

    override fun getMyQuests(): List<Quest> {
        if (!mapOfTakenQuests.containsKey(token)) {
            return emptyList()
        }

        return mapOfTakenQuests[token]!!.toList()
    }

    override fun getAllQuests(feedScope: FeedScope): List<Quest> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllQuests(groupID: Int): List<Quest> {
        return listOfQuests
    }

    override fun getQuests(groupID: Int, from: Int, to: Int): List<Quest> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQuests(scope: FeedScope, from: Int, to: Int): List<Quest> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun isQuestNameCorrect(name: String) = name.length >= Constants.QUEST_NAME_MINIMUM_SYMBOLS
            && name.length <= Constants.QUEST_NAME_MAXIMUM_SYMBOLS

    private fun isQuestDescCorrect(description: String) = description.length >= Constants.QUEST_DESC_MINIMUM_SYMBOLS
            && description.length <= Constants.QUEST_DESC_MAXIMUM_SYMBOLS
}
*/
