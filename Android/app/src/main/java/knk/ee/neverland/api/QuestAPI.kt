package knk.ee.neverland.api

import knk.ee.neverland.api.models.QuestToSubmit
import knk.ee.neverland.models.Quest

interface QuestAPI {
    var token: String

    fun submitNewQuest(quest: QuestToSubmit)

    fun takeQuest(id: Int)

    fun dropQuest(id: Int)

    fun getQuest(id: Int): Quest

    fun getMyQuests(): List<Quest>

    fun getQuestsToTake(): List<Quest>

    fun getSuggestedByUserQuests(userID: Int): List<Quest>

    fun getGroupQuests(groupID: Int): List<Quest>
}
