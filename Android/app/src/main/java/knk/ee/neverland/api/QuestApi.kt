package knk.ee.neverland.api

import knk.ee.neverland.models.Quest
import knk.ee.neverland.models.Solution

interface QuestApi {
    fun submitNewQuest(quest: Quest)

    fun submitSolution(solution: Solution)

    fun getQuest(id: Int): Quest

    fun getAllQuests(feedScope: FeedScope): List<Quest>

    fun getAllQuests(groupID: Int): List<Quest>

    fun getQuests(groupID: Int, from: Int, to: Int): List<Quest>

    fun getQuests(scope: FeedScope, from: Int, to: Int): List<Quest>
}
