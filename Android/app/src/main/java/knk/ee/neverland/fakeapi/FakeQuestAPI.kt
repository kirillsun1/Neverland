package knk.ee.neverland.fakeapi

import knk.ee.neverland.api.FeedScope
import knk.ee.neverland.api.QuestApi
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.pojos.Quest
import knk.ee.neverland.pojos.Solution

class FakeQuestAPI : QuestApi {
    var key: String = ""

    private val QUEST_NAME_MINIMUM_SYMBOLS: Int = 5
    private val QUEST_DESC_MINIMUM_SYMBOLS: Int = 5
    private val QUEST_NAME_MAXIMUM_SYMBOLS: Int = 30
    private val QUEST_DESC_MAXIMUM_SYMBOLS: Int = 480

    private var questList: List<Quest> = mutableListOf<Quest>()
    private var solutionList: List<Solution> = mutableListOf<Solution>()

    override fun submitNewQuest(quest: Quest) {
        if (quest.name.orEmpty().isBlank() || quest.name!!.length < QUEST_NAME_MINIMUM_SYMBOLS
                || quest.name!!.length > QUEST_NAME_MAXIMUM_SYMBOLS) {
            throw QuestAPIException("Name incorrect");
        }

        if (quest.description.orEmpty().isBlank() || quest.description!!.length < QUEST_DESC_MINIMUM_SYMBOLS
                || quest.description!!.length > QUEST_DESC_MAXIMUM_SYMBOLS) {
            throw QuestAPIException("Desc incorrect");
        }

        // TODO: check if group exists

        questList += quest
    }

    override fun submitSolution(solution: Solution) {
        solutionList += solution
    }

    override fun getQuest(id: Int): Quest {
        return questList.getOrElse(id) {
            throw QuestAPIException("No such quest")
        }
    }

    override fun getAllQuests(feedScope: FeedScope): List<Quest> {
        return questList // TODO: filter by scope
    }

    override fun getAllQuests(groupID: Int): List<Quest> {
        return questList
                .filter { quest -> quest.groupID == groupID }
    }

    override fun getQuests(groupID: Int, from: Int, to: Int): List<Quest> {
        if (from > to)
            throw QuestAPIException("Incorrect from/to values")

        return questList
                .subList(from, to)
                .filter { quest -> quest.groupID == groupID }
    }

    override fun getQuests(scope: FeedScope, from: Int, to: Int): List<Quest> {
        if (from > to)
            throw QuestAPIException("Incorrect from/to values")

        return questList
                .subList(from, to)
        // .filter { quest -> quest.groupID == groupID } // TODO: filter by scope
    }
}
