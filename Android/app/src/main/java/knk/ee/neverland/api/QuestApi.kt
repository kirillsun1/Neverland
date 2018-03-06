package knk.ee.neverland.api

import knk.ee.neverland.models.Proof
import knk.ee.neverland.models.Quest

interface QuestApi {
    var token: String

    fun submitNewQuest(quest: Quest)

    fun submitProof(proof: Proof)

    fun takeQuest(id: Int)

    fun dropQuest(id: Int)

    fun getQuest(id: Int): Quest

    fun getMyQuests(): List<Quest>

    fun getQuestsToTake(): List<Quest>
}
